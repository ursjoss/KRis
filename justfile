@_list:
    just --list --unsorted

write-verification-metadata:
    ./gradlew --write-verification-metadata sha256,sha512 help

