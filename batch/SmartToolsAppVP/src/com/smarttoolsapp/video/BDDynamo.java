package com.smarttoolsapp.video;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.SdkBaseException;
import software.amazon.awssdk.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDBClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DynamoDBException;
import software.amazon.awssdk.services.dynamodb.model.InternalServerErrorException;
import software.amazon.awssdk.services.dynamodb.model.ItemCollectionSizeLimitExceededException;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughputExceededException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class BDDynamo implements IBDApp{
	
	DynamoDBClient client;
	public void conectar() throws URISyntaxException{
		if(client == null)
		{
			if(Parametros.isNoSqlRemoto())
				client = DynamoDBClient.create();
			else
				client = DynamoDBClient.builder().endpointOverride(new URI("http://localhost:8000")).build();
		}
	}
	public void Desconectar() {
		
		if(client != null )
			client.close();
	}
	public boolean isConectado() {
		
		return(client != null);
	}
	@Override
	public List<Video> consultarVideosXConvertir() {
		
		List<Video> videos = new ArrayList<>();
		try {
			conectar();
			Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
			values.put(":value", AttributeValue.builder().n("0").build());
			
			Map<String, String> atrr = new HashMap<String, String>();
			atrr.put("#state", "state");
			
			ScanRequest scanRequest = ScanRequest.builder()
					.tableName("Videos").select("ALL_ATTRIBUTES")
					.filterExpression("#state = :value")
					.expressionAttributeNames(atrr)
					.expressionAttributeValues(values)
					.build();
			 ScanResponse resp = client.scan(scanRequest);
			 
			 for(Map<String, AttributeValue> item : resp.items()){
				 try {
					videos.add(Video.fromMapAttribute(item));
				} catch (Exception e) {
					e.printStackTrace();
				}
			 }
		} catch (ProvisionedThroughputExceededException e) {
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (InternalServerErrorException e) {
			e.printStackTrace();
		} catch (DynamoDBException e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		} catch (SdkBaseException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		 return(videos);
	}

	@Override
	public boolean actualizarEstadoVideo(Video video) {
		
		try {
			conectar();
			
			// Llave
			Map<String, AttributeValue> mapKey = new HashMap<String, AttributeValue>();
			mapKey.put("concurso_id",  AttributeValue.builder().s(video.concursoId).build());
			mapKey.put("video_id",  AttributeValue.builder().s(video.videoId).build());
			
			Map<String, String> atrr = new HashMap<String, String>();
			atrr.put("#state", "state");
			
			Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
			values.put(":value", AttributeValue.builder().n("2").build());
			values.put(":value2", AttributeValue.builder().s(video.convertido_file_name).build());
			
			
			
			UpdateItemRequest update = UpdateItemRequest.builder()
					.tableName("Videos")
					.key(mapKey)
					.updateExpression("SET #state=:value, fileNameConv=:value2")
					.expressionAttributeNames(atrr)
					.expressionAttributeValues(values)
					.build();
			
			client.updateItem(update);
			
			return(true);
			
		} catch (ConditionalCheckFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProvisionedThroughputExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ItemCollectionSizeLimitExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DynamoDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SdkClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SdkBaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean CambiarVideoAConvirtiendo(Video video) {
		try {
				conectar();
				
				// Llave
				Map<String, AttributeValue> mapKey = new HashMap<String, AttributeValue>();
				mapKey.put("concurso_id",  AttributeValue.builder().s(video.concursoId).build());
				mapKey.put("video_id",  AttributeValue.builder().s(video.videoId).build());
				
				Map<String, String> atrr = new HashMap<String, String>();
				atrr.put("#state", "state");
				
				Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
				values.put(":value", AttributeValue.builder().n("1").build());
				
				
				
				UpdateItemRequest update = UpdateItemRequest.builder()
						.tableName("Videos")
						.key(mapKey)
						.updateExpression("SET #state=:value")
						.expressionAttributeNames(atrr)
						.expressionAttributeValues(values)
						.build();
				
				client.updateItem(update);
				
				return(true);
			
		} catch (ConditionalCheckFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProvisionedThroughputExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ItemCollectionSizeLimitExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DynamoDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SdkClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SdkBaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
}
