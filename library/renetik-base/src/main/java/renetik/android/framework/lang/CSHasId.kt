package renetik.android.framework.lang

interface CSHasId {
    val id: String
}

fun CSId(id: String): CSHasId = CSIdImplementation(id)

private data class CSIdImplementation(override val id: String) : CSHasId