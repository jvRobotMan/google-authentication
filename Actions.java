package googleSAOps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class gsuiteActions(){
  public static Object uploadMultiPartFile(String filePath, String accessToken, String folderId) throws IOException {
		try {
			File file = new File(filePath);
			String boundary = Long.toHexString(System.currentTimeMillis()); // Generate a unique boundary string

			URL url = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "multipart/related; boundary=" + boundary);
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);
      
      //metadata
			try (OutputStream outputStream = connection.getOutputStream()) {
				// Metadata part
				outputStream.write(("--" + boundary + "\r\n").getBytes());
				outputStream.write(("Content-Type: application/json; charset=UTF-8\r\n\r\n").getBytes());
				outputStream.write(("{ \"name\": \"" + file.getName() + "\", \"parents\": [\"" + folderId + "\"] }\r\n")
						.getBytes());

				// File content part
				outputStream.write(("--" + boundary + "\r\n").getBytes());
				outputStream.write(("Content-Type: " + "application/json" + "\r\n\r\n").getBytes());

        //read file from filepath
				try (FileInputStream fileInputStream = new FileInputStream(file)) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = fileInputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
				}

				// End of multipart content
				outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			}

      //upload file to google drive
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("File uploaded successfully.");
				
			} else {
				System.err.println("Failed to upload file. Response code: " + responseCode);
				System.out.println(connection.getResponseMessage());
			}
			return responseCode + " " + connection.getResponseMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
