//
//  Wait.m
//  TaxiPhone
//
//  Created by Herivelto Santos on 6/8/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "PopUp.h"

@implementation PopUp

UIAlertView *caixaAviso;
UIActivityIndicatorView *progresso;
UIImageView *sinalConexao;


// CAIXA DE MENSAGEM AGUARDE... PARA CONEXAO, PROCESSAMENTO, ATIVACAO
+(void)starPopUp:(NSString*)mensagem{
    // Mostra a caixa de conectando a operadora
    caixaAviso = [[UIAlertView alloc] initWithTitle:nil message: NSLocalizedString(mensagem, nil) delegate:self cancelButtonTitle:nil otherButtonTitles:nil];                    
    progresso= [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(120,70,35,35)];
    sinalConexao = [[UIImageView alloc] initWithFrame:CGRectMake(130,80,15,15)];
    progresso.activityIndicatorViewStyle = UIActivityIndicatorViewStyleWhiteLarge;
    [caixaAviso addSubview:progresso];
    [caixaAviso addSubview:sinalConexao];
    [progresso startAnimating];
    [caixaAviso show];
}


// ESCONDE A CAIXA DE MENSAGEM AGUARDE (Método: exibirCaixaProcessamento)
+(void)stopPopUp{
    [caixaAviso dismissWithClickedButtonIndex:0 animated:NO]; 
    // Se colocar index -1, ocorre ERRO: wait_fences: failed to receive reply: 10004003
}

@end
