@_list:
    just --list --unsorted

# Runs the regular verification-meta update with sha256 and sha512 sums
write-verification-metadata:
    ./gradlew --write-verification-metadata sha256,sha512 help 

# Fetch Maven Central checksums and print a verification-metadata.xml snippet
# Usage: just maven-checksums com.fasterxml.jackson jackson-base 2.22.2
maven-checksums group artifact version:
    #!/usr/bin/env bash
    set -euo pipefail
    path="{{ replace(group, '.', '/') }}/{{ artifact }}/{{ version }}/{{ artifact }}-{{ version }}.pom"
    base_url="https://repo1.maven.org/maven2/${path}"
    sha256=$(curl -fsSL "${base_url}.sha256")
    sha512=$(curl -fsSL "${base_url}.sha512")
    echo ""
    echo "<!-- Add inside <components> in gradle/verification-metadata.xml -->"
    echo "<component group=\"{{ group }}\" name=\"{{ artifact }}\" version=\"{{ version }}\">"
    echo "   <artifact name=\"{{ artifact }}-{{ version }}.pom\">"
    echo "      <sha256 value=\"${sha256}\" origin=\"Maven Central\"/>"
    echo "      <sha512 value=\"${sha512}\" origin=\"Maven Central\"/>"
    echo "   </artifact>"
    echo "</component>"
