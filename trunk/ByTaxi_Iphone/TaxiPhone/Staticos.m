//
//  Staticos.m
//  AnotoTudoIphone
//
//  Created by herivelto on 10/25/10.
//  Copyright 2010 Hg Tecnologia. All rights reserved.
//

#import "Staticos.h"
#import "User.h"


@implementation Staticos

//=================================================================================================================================
// - String Statica com as Url usadas no sistema 
//
//=================================================================================================================================
static NSString *URL = @"http://bytaxi.homedns.org:8080/taxiWeb";
//static NSString *URL = @"http://localhost:8080/taxiWeb"; 
static NSString * _URL_AUTENTICACAO =  @"/seam/resource/rest/login/autenticar/";
static NSString * _URL_SOLICITACOES_PENDENTES = @"/seam/resource/rest/solicitacaoTaxi/solicitacoesPendentes/";
static NSString * _URL_RESERVAR_SOLICITACAO = @"/seam/resource/rest/solicitacaoTaxi/reservaSolicitacao/";
static NSString * _URL_ATUALIZA_POSICAO = @"/seam/resource/rest/solicitacaoTaxi/atualizaPosicaoTaxista/";
static NSString * _URL_HISTORICO = @"/seam/resource/rest/solicitacaoTaxi/historicoSolicitacoes/";


static int _STATUS_CODE_200 = 200;


//=================================================================================================================================
// - Métodos que retorna as url's montadas com 
//
//=================================================================================================================================

+(NSString*)URL_HISTORICO{
    return  [self montaURL:_URL_HISTORICO];
}

+(NSString*)URL_ATUALIZA_POSICAO{ 
 return [self montaURL:_URL_ATUALIZA_POSICAO];
    
}   

+(NSString*)URL_SOLICITACOES_PENDENTES {
	return [self montaURL:_URL_SOLICITACOES_PENDENTES];
}

+(NSString*)URL_RESERVAR_SOLICITACAO {
	return [self montaURL:_URL_RESERVAR_SOLICITACAO];
}


+(int)STATUS_CODE_200 {
	return _STATUS_CODE_200;
}


//Na autenticação é um pouco diferente, nao existe o email cadastrado ainda.
+(NSString*)URL_AUTENTICACAO {
	NSMutableString *urlFinal = [NSMutableString stringWithString:URL];
	[urlFinal appendString:_URL_AUTENTICACAO];	
	return urlFinal;
}


//=================================================================================================================================
// - montaURL 
//   monta a Url localmente com URL + "Da Ação chamada" 
//=================================================================================================================================


+(NSString *)montaURL:(NSString *)urlRecebida{
	NSMutableString *urlFinal = [NSMutableString stringWithString:URL];
	[urlFinal appendString:urlRecebida];	
    NSString * email = [User getLogin];
    NSString *emailAlterado  = [email stringByReplacingOccurrencesOfString:@"@" withString:@"%40"];
	//Adiciona o email do usuário
	[urlFinal appendString:emailAlterado];	
	return 	urlFinal;
}


@end
