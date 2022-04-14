package br.senai.sp.cfp138.guiderest.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FirebaseUtil {
	// variavel para guardar as credenciais de acesso
	private Credentials credential;
	//variavel para acessar e manipular o storage
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
		Resource resource = new ClassPathResource("chave-firebase.json");

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

	// metodo que faz o upload
	public String upload(MultipartFile arquivo) throws IOException {
		// gera um random de string aleatorio para o aquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());

		// criar um blob ID através do nome gerado para o arquivo
		BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);

		// criar o blobInfo através do blobId
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

		// gravar o blobInfo no Storage passando os bytes do arquivo
		storage.create(blobInfo, arquivo.getBytes());

		// retorna a URL do arquivo gerado no Storage
		return String.format(DOWNLOAD_URL, nomeArquivo);
	}
	
	//metodo que exclui o arquivo do storage
	public void deletar(String nomeArquivo) {
		
		//retirar o prefixo e o sulfixo da string
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SULFFIX, "");
		
		//obter um blob através do nome
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
		
		//deleta através do blob
		storage.delete(blob.getBlobId());
		
	}
}
