package br.unesp.academico.analise.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import br.unesp.academico.analise.vo.UnidadeUniversitariaVO;
import br.unesp.core.ConfigHelper;
import br.unesp.exception.ServiceValidationException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnidadeUniversitariaService {
	private String urlUnespAuth;
	private String urlPublicoApi;
	private String clientId;
	private String clientSecret;
	
	public UnidadeUniversitariaService(){
		urlUnespAuth = ConfigHelper.get().getString("sso.unespauth");
		urlPublicoApi = ConfigHelper.get().getString("sso.publicoapi");
		clientId = ConfigHelper.get().getString("oauth.clientId");
    	clientSecret = ConfigHelper.get().getString("oauth.clientSecret");
    	
    	if(urlUnespAuth == null){
    		throw new RuntimeException("A propriedade 'sso.unespauth' não foi encontrada no arquivo config.properties.");
    	}
    	if(urlPublicoApi == null){
    		throw new RuntimeException("A propriedade 'sso.publicoapi' não foi encontrada no arquivo config.properties.");
    	}
    	if(clientId == null){
    		throw new RuntimeException("A propriedade 'oauth.clientId' não foi encontrada no arquivo config.properties.");
    	}
    	if(clientSecret == null){
    		throw new RuntimeException("A propriedade 'oauth.clientSecret' não foi encontrada no arquivo config.properties.");
    	}
	}
	
	public List<UnidadeUniversitariaVO> listarUnidades() throws ServiceValidationException {
		List<UnidadeUniversitariaVO> listaVO = new ArrayList<UnidadeUniversitariaVO>(); 
		String url = urlPublicoApi.concat("/unidadesUniversitarias/");
    	try {
			HttpResponse<JsonNode> jsonUnidade = Unirest.get(url)
					.header("Authorization", "Bearer " + getAccessTokenByClientCredentials())
					.asJson();
			if(jsonUnidade.getStatus() == 200){
				JSONArray unidades = jsonUnidade.getBody().getArray();
				for(int i = 0; i < unidades.length(); i++) {
					JSONObject json = unidades.getJSONObject(i);
					UnidadeUniversitariaVO vo = new UnidadeUniversitariaVO();
					vo.setIdUnidadeUniversitaria(json.getLong("idUnidadeUniversitaria"));
					vo.setNome(json.getString("nome"));
					vo.setNomeAbreviado(json.getString("nomeAbreviado"));
					listaVO.add(vo);
				}
				return listaVO;
			}else if(jsonUnidade.getStatus() == 404){
				return null;
			}else{
				throw new ServiceValidationException("Ocorreu um erro ao buscar unidades");
			}
		} catch (UnirestException e) {
			throw new ServiceValidationException("Ocorreu um erro ao buscar unidades", e);
		}
	}
	
	/**
     * Autentica a aplicação via client credentials
     * @return
     * @throws ServiceValidationException 
     */
    public String getAccessTokenByClientCredentials() throws ServiceValidationException {
    	try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(urlUnespAuth.concat("/oauth/token"))
					.basicAuth(this.clientId, this.clientSecret)
					.field("grant_type", "client_credentials")
					.asJson();
			if(jsonResponse.getStatus() == 401){
				throw new ServiceValidationException("Erro ao autenticar no UnespAuth: Client Id ou Client Secret inválido(a)");
			}
			else if(jsonResponse.getStatus() == 200){
				JSONObject json = new JSONObject(jsonResponse.getBody().toString());
				String accessToken = json.getString("access_token");
				if(accessToken.equals("")){
					throw new ServiceValidationException("Erro ao autenticar no UnespAuth: Ocorreu um erro ao  obter o Access Token via Client Credentials ");
				}
				return accessToken;
			}
		} catch (UnirestException e) {
			throw new ServiceValidationException("Erro insperado ao autenticar no UnespAuth", e);
		}
    	throw new ServiceValidationException("Erro insperado ao autenticar no UnespAuth");
	}
}
