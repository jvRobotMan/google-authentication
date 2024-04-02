package googleOAuth;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GsuiteAuthentication {
	public static String generateJWT(String private_key, String client_email) {
//		  String private_key_id = data.get("private_key_id");
//		String private_key = data.get("googlePrivateKey");
//		String client_email = data.get("googleEmail");
//		  String auth_uri = data.get("auth_uri");
//		  String token_uri = data.get("token_uri");
		String token_uri = "https://oauth2.googleapis.com/token";
//		  String universe_domain = data.get("universe_domain");
//		  String scopes = data.get("scopes");
		String scopes = "https://www.googleapis.com/auth/drive";
	
//		private_key = "-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCcWa26g/oaRsNs\\nVz2wRc3vbxk9wYTsDafePqrNHgGYQVWdhj7gFj6Yyq1nUbsTJY2DOaSd+MllIwZB\\ngns8xRH4alZ06SSyMKgWyXUhZzumqvqm0fcE9A1k1YjkxRaWdl//cqtWCPysTT/X\\nzwY5LgXSPwxFH2KG5nr+cRBGzJ52fIzQeFameEzym0xVe5FJgdvVC30w2ezxPooY\\nCA2Si3IfWXLskxiMSW8wfQlGeANKMlNPSNuKqgVGUPBsEW1gZowUZxgZjo9fSrb/\\nLv7YYBk1Uj0h2dFrmkvK9Wo7s1GpEk43TJmyK8dnWLGZHgc5HQm8tBJiFpxryOHP\\nlqaUT4c7AgMBAAECggEAEKfXA38KtiDisZhrmGsfQIn3yOXkKJdd0iWf4WD4b4VK\\nPxEtgoasHW+94DtLMAN4m0mZOUDnB3CfOnxRb4xY2TtOwX6Sq67WQTMt2KScLKmb\\nMQk+BNeQ07SRtDkwqk+q81X+XZhwflRuYCmMMqwt75NK1dQ9lTxZTuCxiLFEkj+Q\\ntqNrpBclV+tIVJn1BUZMaitzKELsJM+e/gYc6PjFHp8G8kaL0R2J6U51mVlC4MVB\\nhj6iiKIpISMFIC5WaVoCGUgY3r4E6j5JY+fARtk6UbDRmzmhBgwVR4TFX2nPIVzI\\n/OKzhEyVH5bhNywLKGK6CE6ugZoqir/D4ikA+V1XFQKBgQDUhdIIJ0f51o4oW/0+\\nVTmYv1y0J6QXEvNbic+rdrnOtOOSRdXoT/Y41cdkh+lMlnkB+zUYG5KcwKQL4Fyd\\n8HLjPPBZoKTcIoqVGBK/pQ/Y3qTiwVuoKVgBGdzOnJWpIuPCirjz05u+iEZtWJLx\\nJwUan5brYNEuqKbgXplBR15+fwKBgQC8VgMMh7QhpEBSBBR+p4YT8t8CsG7udtez\\nv3r0/7lvE2eJqbWc7tmjMQ8eJFXn9vyvWJTheUvjriImAnn0SDTNeyTlDaTLaRM8\\nf6U7rTeaoymJrW7FCa0Aw8IqkPk6CUGCNRq4JgaMsA/7Y+aBseFIaxAa8vImUSy5\\n7Cm0WA8RRQKBgC5di7cPqtpM/vK7LwLcYRZ8GWOMinYmQkWkSVRlMKByQnv1FHf8\\nigFYUooSRMnawke2YJ0D1vav6JpWtp4WiVoeP2mubIlCV+2zNNPu1QhBesOl37NO\\nYc7Lr6q76sLdw8b7sOIxNndZD9RnUJegJbLrQmfT1Y9SaCXnPZRKV8lNAoGBAIlF\\nyAMiBlFe/uLIxodqY3e21oWHQ9PcG2yQbHaWzfoAEr9sS0uPbGJeJ764jUT96T+F\\nOT7+wim5inpZ47oBxzrePw4U28DHXZYDE9RjdNvpgSUwnERRwR+Fj8im0oFSrcTS\\nIB+I181DpuJ89k4h8SpvntWFrqnvEMKTN/KTHKhhAoGAEx6IaKXaNJVblMeGEMUq\\nIb37quG/U7QTRrBGcI2q0jEN4zK2QhF7H+zunxZL70RdzoCs9RJFStpyKHc4i9Ob\\ntSofUk4SQg+EKlnI7O0Amsui/KTVyDBfAQTIHmdhmCEQLvlTlU+TImZW2iGc04Pc\\n1QkK8GqRlrxHiwMN31Fb23k=\\n-----END PRIVATE KEY-----\\n";
		long now = System.currentTimeMillis();
		try {
			Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) StringToPrivateKey(private_key));

			String signedJWT = JWT.create()
//					.withKeyId(private_key_id)
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
