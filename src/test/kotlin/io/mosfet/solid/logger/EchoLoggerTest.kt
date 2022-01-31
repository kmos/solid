package io.mosfet.solid.logger

import io.mosfet.solid.core.listener.AlreadyRan
import io.mosfet.solid.core.listener.Executed
import io.mosfet.solid.core.listener.ExecutionData
import io.mosfet.solid.core.listener.ExecutionFailed
import io.mosfet.solid.core.listener.Failed
import io.mosfet.solid.core.listener.InvalidChecksum
import io.mosfet.solid.core.listener.RollbackExecuted
import io.mosfet.solid.core.listener.Skipped
import liquibase.changelog.ChangeSet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EchoLoggerTest {

    @Test
    internal fun `given an executed Changeset, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Executed changeset reason_query::author, in aFilePath")
            assertThat(newline).isTrue
            assertThat(error).isFalse
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(EXECUTED)
    }

    @Test
    internal fun `given a changeset with invalid checksum, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Invalid checksum for changeset reason_query::author, in aFilePath")
            assertThat(newline).isTrue
            assertThat(error).isTrue
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(INVALID_CHECKSUM)
    }

    @Test
    internal fun `given a changeset already ran, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Changeset already ran reason_query::author, in aFilePath")
            assertThat(newline).isTrue
            assertThat(error).isFalse
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(ALREADY_RAN)
    }

    @Test
    internal fun `given a changeset with failed, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Failed execution for changeset reason_query::author, in aFilePath")
            assertThat(newline).isTrue
            assertThat(error).isTrue
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(FAILED_EXECUTION)
    }

    @Test
    internal fun `given a changeset to rollback, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Rollback executed for changeset reason_query::author, in aFilePath")
            assertThat(newline).isTrue
            assertThat(error).isFalse
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(ROLLBACK)
    }

    @Test
    internal fun `given a changeset skipped, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Skipped execution for changeset reason_query::author, in aFilePath")
            assertThat(newline).isTrue
            assertThat(error).isFalse
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(SKIPPED)
    }

    @Test
    internal fun `given a failing execution, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Failed to execute changeset reason_query::author, in aFilePath. ooops")
            assertThat(newline).isTrue
            assertThat(error).isTrue
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(ExecutionFailed(
            ChangeSet(
                "reason_query",
                "author",
                true,
                true,
                "aFilePath",
                null,
                null,
                null
            ),
            RuntimeException("ooops")
        ))
    }

    @Test
    internal fun `given a failing execution without exception, when logging, format it correctly`() {
        val underTest = EchoLogger { message, newline, error, lineSeparator, ->
            assertThat(message).isEqualTo("Failed to execute changeset reason_query::author, in aFilePath. Caused by an unknown reason.")
            assertThat(newline).isTrue
            assertThat(error).isTrue
            assertThat(lineSeparator).isEqualTo("\n")
        }

        underTest.handle(ExecutionFailed(
            ChangeSet(
                "reason_query",
                "author",
                true,
                true,
                "aFilePath",
                null,
                null,
                null
            ),
            null
        ))
    }

    companion object {

        private val EXECUTED = ExecutionData(ChangeSet(
        "reason_query",
        "author",
        true,
        true,
        "aFilePath",
        null,
        null,
        null
        ), Executed)

        private val SKIPPED = ExecutionData(ChangeSet(
            "reason_query",
            "author",
            true,
            true,
            "aFilePath",
            null,
            null,
            null
        ), Skipped)

        private val ROLLBACK = ExecutionData(ChangeSet(
            "reason_query",
            "author",
            true,
            true,
            "aFilePath",
            null,
            null,
            null
        ), RollbackExecuted)

        private val INVALID_CHECKSUM = ExecutionData(ChangeSet(
            "reason_query",
            "author",
            true,
            true,
            "aFilePath",
            null,
            null,
            null
        ), InvalidChecksum)

        private val ALREADY_RAN = ExecutionData(ChangeSet(
            "reason_query",
            "author",
            true,
            true,
            "aFilePath",
            null,
            null,
            null
        ), AlreadyRan)

        private val FAILED_EXECUTION = ExecutionData(ChangeSet(
            "reason_query",
            "author",
            true,
            true,
            "aFilePath",
            null,
            null,
            null
        ), Failed)
    }
}