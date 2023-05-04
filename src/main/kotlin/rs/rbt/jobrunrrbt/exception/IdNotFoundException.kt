package rs.rbt.jobrunrrbt.exception

/* The class `IdNotFoundException` is a custom exception that extends the `RuntimeException` class and
can be used to handle cases where an ID is not found. */
class IdNotFoundException(message: String? = null) : RuntimeException(message)
