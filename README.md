# Symbv
Validating Code Changes Using Symbolic Execution

[Notes](https://docs.google.com/document/d/1R1IoixQTR7sgV6iQxtHaf7gT7ey2lt-3bxzXLA-vkag/edit?usp=sharing)

[Report](https://docs.google.com/document/d/1F_Lle-M_2aHqeiU5WjstN4JEk1EeNFOdXqEE5TV5Kas/edit?usp=sharing)

## Requirements
- Java 8
- [Z3](https://github.com/psycopaths/jConstraints-z3#building-and-installing-z3)

## Installation
- `ant clean`
- `ant bootstrap`
- `ant resolve`
- `ant build`

## Usage

To run any of the examples (or another project on a separate directory in the examples directory) you could use the
shell script provided:

```
# Apply patches and generate Symbv tests
./bin/symbv example_name gen

# Compile everything
ant build

# Concolic/Symbolic execution
./bin/symbv example_name symbv
```

The last command could be replaced by two other options (for prune optimization or full symbolic execution):

```
# Prune Optimization
./bin/symbv example_name prune

# Symbolic execution
./bin/symbv example_name symbolic
```

## Development Info
### Tasks
- Run `ant bootstrap` - boostraps ivy install
- Run `ant resolve` - downloads the dependencies and places them in the lib directory
- Run `ant build` - builds the project
- Run `ant test` - runs test cases

### Adding Dependencies
1. Add dependency to `ivy.xml`
2. Run `ant resolve`
3. Add new jars to `.classpath` under the 3rd party jars section (this could be useful: `cd lib; for f in *.jar; do echo "<classpathentry kind=\"lib\" path=\"lib/$f\"/>"; done`)
