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
}
