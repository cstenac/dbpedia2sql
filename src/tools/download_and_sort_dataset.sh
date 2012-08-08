#! /bin/sh

PREFIX=http://downloads.dbpedia.org/3.7/

SOURCE=$1
TARGET=$2

# Required for sort to sort on UTF8 data
export LANG=C

wget http://downloads.dbpedia.org/3.7/$SOURCE -O /dev/stdout|bunzip2 -c|sort|gzip -c > $TARGET
