package br.senai.sp.cfp138.guiderest.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class FirebaseUtil {
	// variavel para guardar as credenciais de acesso
	private Credentials credential;

	private Storage storage;
	// constante para o nome do bucket
	private final String BUCKET_NAME = "guiderestmaick.appspot.com";
	// constante para o prefixo da URL
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
	// constante para o sulfixo da URL
	private final String SULFFIX = "?alt=media";
	// constante para a URL
	private final String DOWNLOAD_URL = PREFIX + "%s" + SULFFIX;

	public FirebaseUtil() {
		// acessar o arquivo json com a chave privada
		Resource resource = new ClassPathResource("chavefirebase.json");

		try {
			// gera uma credencial no firebase através da chave do arquivo
			credential = GoogleCredentials.fromStream(resource.getInputStream());

			// cria o storage para manipular os dados do firebase
			storage = StorageOptions.newBuilder().setCredentials(credential).build().getService();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}

	}

	// metodo para extrair a extensao do arquivo
	private String getExtensao(String nomeArquivo) {
		// extrai o trecho do arquivo onde está a extensao
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
	
	
	//metodo que faz o upload
	public String upload(MultipartFile arquivo) {
		//gera um random de string aleatorio para o aquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		return "";
	}
}
