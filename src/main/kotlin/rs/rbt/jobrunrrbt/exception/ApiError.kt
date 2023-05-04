package rs.rbt.jobrunrrbt.exception

/**
 * The above code defines a data class named ApiError with two properties: status and message.
 * @property {Int} status - The HTTP status code returned by an API endpoint. It indicates the success
 * or failure of the request made to the API. For example, a status code of 200 means the request was
 * successful, while a status code of 404 means the requested resource was not found.
 * @property {String} message - The message property is a string that represents the error message
 * returned by an API call. It provides information about what went wrong during the request.
 */
data class ApiError(val status: Int, val message: String)
