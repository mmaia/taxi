//
//  User.h
//  AnotoTudoIphone
//
//  Created by herivelto on 10/13/10.
//  Copyright 2010 Hg Tecnologia. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface User : NSObject {
	
}

+(void)gravaUsuario :(NSString *)pId :(NSString *)pLogin :(NSString *)pSenha ;
+(void)limparUsuario;
+(NSString *) getID ;
+(NSString *) getLogin;
+(NSString *) getSenha; 
+(bool) isLogado;
+(void)lerUsuario;
	
@end
