EXAMPLE="$1"
bin/jpf "src/examples/$1/$1.jpf" gen src/examples/$1/*.patch
