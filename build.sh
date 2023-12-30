#! /bin/sh

BIN_DIR="./bin"
SRC_DIR="./src"
REF_LIB="./drivers/mysql-connector-j-8.0.33.jar"
JC="javac -d $BIN_DIR -sourcepath $SRC_DIR -cp $REF_LIB"
EXT="java"

if [ ! -d $BIN_DIR ]
then
	eval "mkdir $BIN_DIR"
fi

filename="$1"
if [ -z $filename ]
then
	read -p "Enter filename: " filename
fi

echo "$JC $SRC_DIR/$filename.$EXT"
eval "$JC $SRC_DIR/$filename.$EXT"
