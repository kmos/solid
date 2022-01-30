package io.mosfet.solid.core.listener

import liquibase.changelog.ChangeSet
import liquibase.changelog.DatabaseChangeLog
import liquibase.changelog.visitor.AbstractChangeExecListener
import liquibase.database.Database

sealed class Execution
data class ExecutionData(val changeSet: ChangeSet, val status: ExecutionStatus): Execution()
data class ExecutionFailed(val changeSet: ChangeSet, val e: Exception?): Execution()

sealed class ExecutionStatus
object NotRun: ExecutionStatus()
object AlreadyRan: ExecutionStatus()
object RunAgain: ExecutionStatus()
object MarkRan: ExecutionStatus()
object InvalidChecksum: ExecutionStatus()
object Executed: ExecutionStatus()
object Failed: ExecutionStatus()
object Skipped: ExecutionStatus()
object Reran: ExecutionStatus()
object Rollback: ExecutionStatus()
object RollbackExecuted: ExecutionStatus()

class ChangeSetListener(
    private val onExecution: (Execution) -> Unit
): AbstractChangeExecListener() {
    override fun willRun(
        changeSet: ChangeSet?,
        databaseChangeLog: DatabaseChangeLog?,
        database: Database?,
        runStatus: ChangeSet.RunStatus?,
    ) {
        if (changeSet != null && runStatus != null) {
            onExecution.invoke(ExecutionData(changeSet, status(runStatus)))
        }
    }

    override fun ran(
        changeSet: ChangeSet?,
        databaseChangeLog: DatabaseChangeLog?,
        database: Database?,
        execType: ChangeSet.ExecType?,
    ) {
        if (changeSet != null && execType != null) {
            onExecution.invoke(ExecutionData(changeSet, status(execType)))
        }
    }

    override fun willRollback(changeSet: ChangeSet?, databaseChangeLog: DatabaseChangeLog?, database: Database?) {
        changeSet
            ?.let { onExecution.invoke(ExecutionData(it, Rollback)) }
    }

    override fun rolledBack(changeSet: ChangeSet?, databaseChangeLog: DatabaseChangeLog?, database: Database?) {
        changeSet
            ?.let { onExecution.invoke(ExecutionData(it, RollbackExecuted)) }
    }

    override fun runFailed(
        changeSet: ChangeSet?,
        databaseChangeLog: DatabaseChangeLog?,
        database: Database?,
        exception: Exception?,
    ) {
        if (changeSet!=null) {
            onExecution.invoke(ExecutionFailed(changeSet, exception))
        }
    }

    override fun rollbackFailed(
        changeSet: ChangeSet?,
        databaseChangeLog: DatabaseChangeLog?,
        database: Database?,
        exception: Exception?,
    ) {
        if (changeSet!=null) {
            onExecution.invoke(ExecutionFailed(changeSet, exception))
        }
    }

    private fun status(runStatus: ChangeSet.RunStatus): ExecutionStatus {
        return when (runStatus) {
            ChangeSet.RunStatus.NOT_RAN -> NotRun
            ChangeSet.RunStatus.ALREADY_RAN -> AlreadyRan
            ChangeSet.RunStatus.RUN_AGAIN -> RunAgain
            ChangeSet.RunStatus.MARK_RAN -> MarkRan
            ChangeSet.RunStatus.INVALID_MD5SUM -> InvalidChecksum
        }
    }

    private fun status(runStatus: ChangeSet.ExecType): ExecutionStatus =
        when (runStatus) {
            ChangeSet.ExecType.EXECUTED -> Executed
            ChangeSet.ExecType.FAILED -> Failed
            ChangeSet.ExecType.SKIPPED -> Skipped
            ChangeSet.ExecType.RERAN -> Reran
            ChangeSet.ExecType.MARK_RAN -> MarkRan
        }
}