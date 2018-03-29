# Symbv
Validating Code Changes Using Symbolic Execution

[Notes](https://docs.google.com/document/d/1R1IoixQTR7sgV6iQxtHaf7gT7ey2lt-3bxzXLA-vkag/edit?usp=sharing)

[Report](https://docs.google.com/document/d/1F_Lle-M_2aHqeiU5WjstN4JEk1EeNFOdXqEE5TV5Kas/edit?usp=sharing)

## Getting Started
- Install `jdart`
- Add `jpf-symbv` to `~/.jpf/site.properties`

e.g.
```
jpf-core = /home/<user>/jpf/jpf-core
jpf-jdart = /home/<user>/jpf/jdart
jpf-symbv = /home/<user>/jpf/symbv
extensions=${jpf-core}
```

## Tasks
- Run `ant bootstrap` - boostraps ivy install
- Run `ant resolve` - downloads the dependencies and places them in the lib directory
- Run `ant build` - builds the project
- Run `ant test` - runs test cases

## Adding Dependencies
1. Add dependency to `ivy.xml`
2. Run `ant resolve`
3. Add the jars to `.classpath` under the 3rd party jars section (copy paste: `for f in *; do echo "<classpathentry kind=\"lib\" path=\"lib/$f\"/>"; done`)

## Example Usage
```
cd symbv
bin/jpf src/examples/basic/test.jpf
```
