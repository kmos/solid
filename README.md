# solid

a CLI that tries to cover a [`dry-run`](https://stackoverflow.com/questions/21847482/does-liquibase-support-dry-run) 
phase for [liquibase](https://liquibase.org/) database change management.

### Usage


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
- [ ] use properly log with echo
- [ ] introduce a secondary parameter, use changelog.xml path instead of default

#### release v0.3

- [ ] check the changesets are equal to another database
- [ ] improve documentation

#### release v0.4

- [ ] decouple drivers for license purpose
- [ ] remove test container as dependency and use java docker API directly

### Contributors

* **Giovanni Panice** - [mosfet.io](https://mosfet.io)

See also the list of [contributors](CONTRIBUTORS.md) who participated in this project.

### License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details