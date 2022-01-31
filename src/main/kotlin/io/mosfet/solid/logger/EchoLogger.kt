package io.mosfet.solid.logger

import io.mosfet.solid.core.listener.AlreadyRan
import io.mosfet.solid.core.listener.Executed
import io.mosfet.solid.core.listener.Execution
import io.mosfet.solid.core.listener.ExecutionData
import io.mosfet.solid.core.listener.ExecutionFailed
import io.mosfet.solid.core.listener.Failed
import io.mosfet.solid.core.listener.InvalidChecksum
import io.mosfet.solid.core.listener.MarkRan
import io.mosfet.solid.core.listener.NotRun
import io.mosfet.solid.core.listener.Reran
import io.mosfet.solid.core.listener.Rollback
import io.mosfet.solid.core.listener.RollbackExecuted
import io.mosfet.solid.core.listener.RunAgain
import io.mosfet.solid.core.listener.Skipped

class EchoLogger(private val echo: (String, Boolean, Boolean, String) -> Unit) : Logger {

    override fun handle(data: Execution) {
        when (data) {
            is ExecutionData -> handleFail(data)
            is ExecutionFailed -> handleFail(data)
        }
    }

    private fun handleFail(data: ExecutionFailed) {
        echo.invoke(failed(data), true, true, "\n")
    }

    private fun handleFail(data: ExecutionData) =
        when (data.status) {
            AlreadyRan -> echo.invoke(alreadyRan(data), true, false, "\n")
            Executed -> echo.invoke(executed(data), true, false, "\n")
            RollbackExecuted -> echo.invoke(rollback(data), true, false, "\n")
            Skipped -> echo.invoke(skipped(data), true, false, "\n")
            InvalidChecksum -> echo.invoke(invalidChecksum(data), true, true, "\n")
            Failed -> echo.invoke(failed(data), true, true, "\n")
            RunAgain, Rollback, MarkRan, NotRun, Reran -> Unit
        }

    private fun failed(data: ExecutionFailed) =
        "Failed to execute changeset ${data.changeSet.id}::${data.changeSet.author}, in ${data.changeSet.filePath}. ${data.e?.message?: "Caused by an unknown reason."}"

    private fun skipped(data: ExecutionData) =
        "Skipped execution for changeset ${data.changeSet.id}::${data.changeSet.author}, in ${data.changeSet.filePath}"

    private fun rollback(data: ExecutionData) =
        "Rollback executed for changeset ${data.changeSet.id}::${data.changeSet.author}, in ${data.changeSet.filePath}"

    private fun failed(data: ExecutionData) =
        "Failed execution for changeset ${data.changeSet.id}::${data.changeSet.author}, in ${data.changeSet.filePath}"

    private fun alreadyRan(data: ExecutionData) =
        "Changeset already ran ${data.changeSet.id}::${data.changeSet.author}, in ${data.changeSet.filePath}"

    private fun invalidChecksum(data: ExecutionData) =
        "Invalid checksum for changeset ${data.changeSet.id}::${data.changeSet.author}, in ${data.changeSet.filePath}"

    private fun executed(data: ExecutionData) =
        "Executed changeset ${data.changeSet.id}::${data.changeSet.author}, in ${data.changeSet.filePath}"
}

