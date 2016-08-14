//
//  LoginViewController.h
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/29/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController : UIViewController <UITextFieldDelegate>  {
	UITextField *txEmail;
	UITextField *txSenha;
	UIActivityIndicatorView *loginIndicator;
	NSMutableData *receivedData;
    
    UILabel *textoInicial;
    UILabel *emailUser;
    UILabel *password;    
    UIButton *btLogin;
	
}

-(void)logar:(id)sender;
-(BOOL)validateEmail:(NSString *)email;
//-(void)stoIndicador;
//-(void)startIndicador;
-(IBAction)doPostRequest ;
-(void)limparUsuario:(id)sender;


@property (nonatomic, retain) IBOutlet UILabel *textoInicial;
@property (nonatomic, retain) IBOutlet UILabel *emailUser;
@property (nonatomic, retain) IBOutlet UILabel *password;
@property (nonatomic, retain) IBOutlet UIButton *btLogin;

@property (nonatomic, retain) NSMutableData *receivedData;
@property (nonatomic, retain) IBOutlet 	UITextField *txEmail;
@property (nonatomic, retain) IBOutlet 	UITextField *txSenha;

@end

