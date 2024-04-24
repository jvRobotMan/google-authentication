## Google Drive Upload with Service Account Authentication

This repository provides two Java classes for uploading files to Google Drive using a service account.

**gsuiteActions.java**

This class contains the `uploadMultiPartFile` method for uploading a file to Google Drive. Here's a breakdown of its functionalities:

1. **Method Parameters:**
    * `filePath`: The path to the file you want to upload.
    * `accessToken`: An access token obtained through Google Service Account authentication (handled by `oauth.java`).
    * `folderId`: (Optional) The ID of the folder where you want to upload the file.

2. **Process:**
    * Creates a multipart HTTP request with a unique boundary.
    * Sets the request URL to the Google Drive upload endpoint.
    * Prepares the metadata part with the file name and optional folder ID.
    * Reads the file content from the specified path.
    * Writes the file content as a separate part in the request.
    * Sends the complete multipart request to Google Drive.
    * Handles the response code:
        * Success (200): Prints a success message.
        * Failure: Prints an error message with the response code and message.
    * Returns a string containing the response code and message.

**oauth.java**

This class handles generating a JSON Web Token (JWT) for Google Service Account authentication.

* **`generateJWT` method:**
    * Takes the private key (in a String format) and client email address as input.
    * Defines the token URI, scopes (permissions), and expiration time.
    * Converts the private key string into a `RSAPrivateKey` object.
    * Creates a JWT with the following claims:
        * Issuer: Client email address
        * Subject: Client email address
        * Audience: Google token endpoint URI
        * Issued at: Current time
        * Scope: Requested permissions (access to Google Drive)
        * Expiration: Current time + 1 hour (you can adjust this)
    * Signs the JWT with the private key.
    * Prints the generated JWT for reference.
    * Returns the JWT string.

**How to Use:**

1.  **Set up Google Service Account:**
    *  Create a service account in the Google Cloud Platform console and download the private key as a JSON file.
2.  **Replace placeholders:**
    *  In `oauth.java`, replace `private_key` with the contents of your private key file (after proper formatting) and `client_email` with your service account email address.
    *  In `gsuiteActions.java`, you'll need to provide the path to your file and the folder ID (optional).
3.  **Call the upload method:**
    *  Obtain the access token using `generateJWT` from `oauth.java`.
    *  Call `uploadMultiPartFile` from `gsuiteActions.java` with the file path, access token, and folder ID (optional).

**Note:** 
* This is a basic example and might require error handling and exception management for real-world applications.
* Ensure proper security measures are taken when storing and using the private key.

**Usage in Dataweave 2.0**
```
%dw 2.0
output application/json
---
// Replace with your actual values
var filePath = "/path/to/your/file.txt";
var serviceAccountEmail = "your-service-account@project-id.iam.gserviceaccount.com";
var privateKey = // Replace with properly formatted private key content

// Call the generateJWT function from oauth.java
var accessToken = oauth.generateJWT(privateKey, serviceAccountEmail);

// Optional: Specify the folder ID for upload
var folderId = "your_folder_id"; // Replace with actual folder ID

// Call the uploadMultiPartFile function from gsuiteActions.java
var uploadResult = gsuiteActions.uploadMultiPartFile(filePath, accessToken, folderId);

// Check the upload result
if (uploadResult.startsWith("200")) {
  write("File uploaded successfully!");
} else {
  write("Upload failed: " ++ uploadResult);
}
```
# Maven dependencies
```
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.3.0</version>
</dependency>
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20240303</version>
</dependency>
```
