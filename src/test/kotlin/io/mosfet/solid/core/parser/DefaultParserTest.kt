package io.mosfet.solid.core.parser

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultParserTest {
    @Test
    internal fun `given a relative path, when retrieving path, return with project dir and relative`() {
        val underTest = DefaultParser("./example/relative/path")

        assertThat(underTest.parse())
            .isEqualTo(SolidPath("./example/relative/path", System.getProperty("user.dir")))
    }

    @Test
    internal fun `given an absolute path, when retrieving path, return a split path`() {
        val underTest = DefaultParser("/example/relative/path")

        assertThat(underTest.parse())
            .isEqualTo(SolidPath("./path", "/example/relative"))
    }
}