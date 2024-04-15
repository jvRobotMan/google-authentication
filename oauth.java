package googleSAOps;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;



public class GsuiteAuthentication {
	public static String generateJWT(String private_key, String client_email) {
		String token_uri = "https://oauth2.googleapis.com/token";
		String scopes = "https://www.googleapis.com/auth/drive";
		long now = System.currentTimeMillis();
		try {
			Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) StringToPrivateKey(private_key));

			String signedJWT = JWT.create()
					.withIssuer(client_email).withSubject(client_email).withAudience(token_uri)
					.withIssuedAt(new Date(now)).withClaim("scope", scopes).withExpiresAt(new Date(now + 3600 * 1000L))
					.sign(algorithm);
			System.out.println(signedJWT);
			return signedJWT;
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}

	private static PrivateKey StringToPrivateKey(String privateKeyString) throws Exception {
		try {
			privateKeyString = privateKeyString.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "")
					.replace("-----END PRIVATE KEY-----", "");

			privateKeyString = privateKeyString.replaceAll("[^a-zA-Z0-9+/=]", "");
			
			byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			return keyFactory.generatePrivate(keySpec);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Object uploadFile(String filePath, String accessToken, String folderId) throws IOException {
		try {
			File file = new File(filePath);
			String boundary = Long.toHexString(System.currentTimeMillis()); // Generate a unique boundary string

			URL url = new URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "multipart/related; boundary=" + boundary);
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);

			try (OutputStream outputStream = connection.getOutputStream()) {
				// Metadata part
				outputStream.write(("--" + boundary + "\r\n").getBytes());
				outputStream.write(("Content-Type: application/json; charset=UTF-8\r\n\r\n").getBytes());
				outputStream.write(("{ \"name\": \"" + file.getName() + "\", \"parents\": [\"" + folderId + "\"] }\r\n")
						.getBytes());

				// File content part
				outputStream.write(("--" + boundary + "\r\n").getBytes());
				outputStream.write(("Content-Type: " + "application/json" + "\r\n\r\n").getBytes());

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
