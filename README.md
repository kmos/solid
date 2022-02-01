# solid

a CLI that tries to cover a [`dry-run`](https://stackoverflow.com/questions/21847482/does-liquibase-support-dry-run)
phase for [liquibase](https://liquibase.org/) database change management.

### Installation

Solid is installed by running one of the following commands in your terminal. You can install this via the command-line
with either ```curl```, ```wget``` or another similar tool.

```shell
sh -c "$(curl -fsSL https://raw.githubusercontent.com/solid/master/tools/install.sh)"

# or

sh -c "$(wget -O- https://raw.githubusercontent.com/solid/master/tools/install.sh)"
```

### Usage

```shell

$ solid --help
Usage: dry-run-clikt [OPTIONS] PATH

Options:
  -h, --help  Show this message and exit

Arguments:
  PATH  path for changesets


```

### Supported Engine

- [X] MySQL
- [ ] msSQL
- [ ] cassandra
- [ ] postgresql
- [ ] h2
- [ ] derby
- [ ] hsqldb
- [ ] db2

## Roadmap

#### release v0.2

- [X] test update and rollback against mysql database
- [X] move it as a CLI
- [X] pack it with jreleaser
- [X] setup correctly CI for release
- [X] make it work with absolute path
- [X] make it work with relative path like ../path
- [X] use properly log with echo
- [X] create installation script

#### release v0.3

- [ ] improve documentation
- [ ] check the changesets are equal to another database
- [ ] introduce a secondary parameter, use changelog.xml path instead of default
- [ ] add support for postgres

#### release v0.4

- [ ] decouple drivers for license purpose
- [ ] remove test container as dependency and use java docker API directly

### License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details