package io.mosfet.solid.validator

import arrow.core.left
import arrow.core.right
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

internal class PathValidatorTest {

    @Test
    internal fun `the path shouldn't be a directory`() {
        assertThat(PathValidator("a path").validate()).isEqualTo(NotAFolder.left())
    }

    @Test
    internal fun `the path shouldn't be root`() {
        assertThat(PathValidator("/").validate()).isEqualTo(Root.left())
    }

    @Test
    internal fun `the path should be an empty directory`() {
        Files.createDirectories(Paths.get("./target/empty"))
        assertThat(PathValidator("./target/empty").validate()).isEqualTo(Empty.left())
    }

    @Test
    internal fun `the path should be a directory with a file`() {
        Files.createDirectories(Paths.get("./target/notEmpty"))
        File("./target/notEmpty/example.txt").createNewFile()

        assertThat(PathValidator("./target/notEmpty").validate()).isEqualTo(Ok.right())
    }
}