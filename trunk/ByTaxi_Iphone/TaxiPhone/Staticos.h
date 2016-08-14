//
//  Staticos.h
//  AnotoTudoIphone
//
//  Created by herivelto on 10/25/10.
//  Copyright 2010 Hg Tecnologia. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Staticos : NSObject {

}

+(NSString*)URL_HISTORICO;
+(NSString*)URL_ATUALIZA_POSICAO;
+(NSString*)URL_AUTENTICACAO;
+(NSString*)URL_SOLICITACOES_PENDENTES;
+(NSString*)URL_RESERVAR_SOLICITACAO;
+(int)STATUS_CODE_200 ;
+(NSString*)montaURL:(NSString *)urlRecebida;

@end
