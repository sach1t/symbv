EXAMPLE="$1"
EXAMPLEDIR="src/examples/$1"
JPFFILE="src/examples/$1/$1.jpf"
JPFFILESYMB="src/examples/$1/$1_symbolic.jpf"
TESTDIR="testdata"
mkdir -p "$TESTDIR"

echo "RUNNING CONCOLIC TESTS"
for i in {1..5}; do
	file="$TESTDIR/$1_run$i.txt"
	statfile="$TESTDIR/$1_stats.txt"
	jpf "$JPFFILE"  "exec" > $file
	grep -F -A 15 "====================================================== statistics" $file | tee -a $statfile
	echo "////////////////////////////////////////////////////////////////////" | tee -a $statfile
done

echo "RUNNING CONCOLIC PRUNING TESTS"
for i in {1..5}; do
	file="$TESTDIR/$1_prune_run$i.txt"
	statfile="$TESTDIR/$1_prune_stats.txt"
	jpf "$JPFFILE"  "exec" "prune" > $file
	grep -F -A 15 "====================================================== statistics" $file | tee -a $statfile
	echo "////////////////////////////////////////////////////////////////////" | tee -a $statfile
done

echo "RUNNING SYMBOLIC TESTS"
for i in {1..5}; do
	file="$TESTDIR/$1_symbolic_run$i.txt"
	statfile="$TESTDIR/$1_symbolic_stats.txt"
	jpf "$JPFFILESYMB" > $file
	grep -F -A 15 "====================================================== statistics" $file | tee -a $statfile
done
