//
//  User.m
//  AnotoTudoIphone
//
//  Created by herivelto on 10/13/10.
//  Copyright 2010 Hg Tecnologia. All rights reserved.
//

#import "User.h"

@implementation User

NSString *ID = NULL;
NSString *login = NULL;
NSString *senha = NULL;	
bool logado = false;


//=================================================================================================================================
// - Ler InformaçÕes do Usuário que está gravada no configuração local
// 
//=================================================================================================================================

+(void)lerUsuario{

	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults]; 
	login = [defaults objectForKey:@"login"];
	senha = [defaults objectForKey:@"senha"];
	ID = [defaults objectForKey:@"id"];	
	defaults = nil;
    
    if (ID == nil) {
        logado = false;
    }else{
        logado = true;
    }
}

//=================================================================================================================================
// - Verifica se o usuário já está logado 
// Return Boolean ( se está logado )
//
//=================================================================================================================================

+(bool) isLogado{
  
    return logado;
}

//=================================================================================================================================
// - Grava InformaçÕes do Usuário que está gravada no configuração local
// @ID
// @Login
// @Senha
//=================================================================================================================================

+(void)gravaUsuario :(NSString *)pId :(NSString *)pLogin :(NSString *)pSenha{
	
	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults]; 
	[defaults setObject:pLogin forKey:@"login"];
	[defaults setObject:pSenha forKey:@"senha"];
	[defaults setObject:pId forKey:@"id"];	
	[defaults synchronize];
	defaults = nil;
	logado = true;
	[self lerUsuario];
}

//=================================================================================================================================
// - Limpar InformaçÕes do Usuário que está gravada no configuração local
//
//=================================================================================================================================

+(void)limparUsuario {
	
	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults]; 
	[defaults setObject:NULL forKey:@"login"];
	[defaults setObject:NULL forKey:@"senha"];
	[defaults setObject:NULL forKey:@"id"];	
	[defaults synchronize];
	defaults = nil;
    logado = false;
    [self lerUsuario];
	
}

//=================================================================================================================================
// - Get para todas propriedades do Login
//
//=================================================================================================================================

+(NSString *) getID {
    
    return  ID;
}

+(NSString *) getLogin{
    
    return login;
}

+(NSString *) getSenha{
    return senha;
}


//=================================================================================================================================


- (void)dealloc {
	
}

@end
