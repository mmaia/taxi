//
//  LoginViewController.m
//  AnotoTudoIphone
//
//  Created by herivelto on 9/13/10.
//  Copyright 2010 Hg Tecnologia. All rights reserved.
//

#import "LoginViewController.h"
#import "Alerts.h"
#import "User.h"
#import "Staticos.h"
#import "PopUp.h"

@implementation LoginViewController
@synthesize txSenha, txEmail;
@synthesize receivedData;
@synthesize textoInicial,emailUser,password,btLogin;

NSString *senha2;
NSString *ID;
int responseStatusCode ;

#pragma mark -
#pragma mark View lifecycle

//=================================================================================================================================
// - Construtor
//
//=================================================================================================================================

- (void)viewDidLoad {
	//Seta  o foco no campo email quando inicia a tela
	[txEmail becomeFirstResponder];
    
	// Ler o usuário a tela ( Caso tenha salvo );	
    
	txEmail.text = [User getLogin];
	txSenha.text = [User getSenha];
	ID   = [User getID];
    
    textoInicial.text = NSLocalizedString(@"TEXTO",nil);
    emailUser.text = NSLocalizedString(@"EMAIL",nil);
    password.text = NSLocalizedString(@"SENHA",nil);
    btLogin.titleLabel.text = NSLocalizedString(@"BOTAO_LOGIN",nil);

	
	[super viewDidLoad];	
}

//=================================================================================================================================
// - Método que mostra uma um popUp pra redigira a senha na hora de criar o usuário no servidor
//  ( Ainda nao está implementado 100% )
//=================================================================================================================================

-(void)alertPassword {
    
	UIAlertView* dialog = [[UIAlertView alloc] init]; 
	[dialog setDelegate:self]; 
	[dialog setTitle:NSLocalizedString(@"POPUP_CRIAR_USUARIO",nil)]; 
	[dialog setMessage:@" "]; 
	
	[dialog addButtonWithTitle:NSLocalizedString(@"OK",nil)]; 
	[dialog addButtonWithTitle:NSLocalizedString(@"CANCELAR",nil)]; 
	
	UITextField *senhaField = [[UITextField alloc] initWithFrame:CGRectMake(20.0, 90.0, 245.0, 25.0)]; 
	[senhaField setBackgroundColor:[UIColor whiteColor]]; 
	[dialog addSubview:senhaField]; 
	CGAffineTransform moveUp = CGAffineTransformMakeTranslation(0,0); 
	[dialog setTransform: moveUp]; 
	[dialog show]; 
	senha2 = senhaField.text;	
    
}	

//=================================================================================================================================
// - Ver qual é o botão que foi clicado Se or OK = Chama o método doPostRequest ( que monta http )
//
//=================================================================================================================================

- (void)alertView:(UIAlertView *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
	// the user clicked one of the OK/Cancel buttons
	if (buttonIndex == 0){

		
        if (txSenha.text == senha2) {
			[self doPostRequest];
		}else {
			[[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                                  NSLocalizedString(@"ALERTA_SENHA_NAO_CONFERE",nil):
                                  NSLocalizedString(@"OK",nil)]; 
		}
	}
}


//=================================================================================================================================
// - Método chamado pelo botão LIMPAR USUário
//
//=================================================================================================================================

-(void)limparUsuario:(id)sender{
    
	txEmail.text = @"";
	txSenha.text = @"";
	[User limparUsuario];
}

//=================================================================================================================================
// - Método chamado pelo botão Logar
//
//=================================================================================================================================

-(void)logar:(id)sender{
	
    
	if ([[txEmail text]length ] > 0 && [[txSenha text]length] > 0 ) {
		//validar o email 
		if ([self validateEmail:txEmail.text]) {
                if (![[txEmail text]isEqualToString:[User getLogin]]) { 
                       [self doPostRequest];
                }else {
                    [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                                          NSLocalizedString(@"LOGIN_FEITO",nil):
                                          NSLocalizedString(@"OK",nil)]; 
                }			
		}else{
            [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                                  NSLocalizedString(@"LOGIN_INVALIDO",nil):
                                  NSLocalizedString(@"OK",nil)]; 

			[txEmail becomeFirstResponder];
		}
	}else {
        [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                              NSLocalizedString(@"CAMPOS_OBRIGATORIOS",nil):
                              NSLocalizedString(@"OK",nil)]; 
	}

}	


//=================================================================================================================================
// - Métodos HTTP que fazem as Chamadas, montam URL e trazem os métodos de Callback para retorno do HTTP
//
//
//=================================================================================================================================

#pragma mark - 
#pragma mark NSURLConnection Callbacks 

-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data { 
	[receivedData appendData:data];
}

-(void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
	self.receivedData = nil;
    [PopUp stopPopUp];
	[[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                          NSLocalizedString(@"ERRO_CONECTANDO",nil):
                          NSLocalizedString(@"OK",nil)]; 

} 

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {

    [PopUp stopPopUp];
	NSString *payloadAsString = [[NSString alloc] initWithData:receivedData encoding:NSUTF8StringEncoding]; 
	ID = payloadAsString;

	//Grava o Usuário Localmente na configuração caso retorno seja OK
	if(responseStatusCode == [Staticos STATUS_CODE_200] ){
		@try {//Tenta Gravar Localmente
			[User gravaUsuario :ID :txEmail.text :txSenha.text];
            
            [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                                    NSLocalizedString(@"LOGADO_COM_SUCESSO",nil):
                                    NSLocalizedString(@"OK",nil)]; 
			[self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:0] animated:YES];
		}@catch (NSException * e) {
            [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                                    NSLocalizedString(@"ERRO_SALVANDO",nil):
                                    NSLocalizedString(@"OK",nil)]; 
		}
	}else{
        [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                                NSLocalizedString(@"USUARIO_SENHA_INVALIDO",nil):
                                NSLocalizedString(@"OK",nil)]; 
	}	
	self.receivedData = nil;
	
}


- (void)connection:(NSURLConnection*)connection didReceiveResponse:(NSURLResponse*)response {
	NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
	responseStatusCode = [httpResponse statusCode];
}


-(IBAction)doPostRequest {
		    
    [PopUp starPopUp:NSLocalizedString(@"LOGIN_CONECTANDO",nil)];

	NSURL *url = [[NSURL alloc] initWithString:[Staticos URL_AUTENTICACAO]]; 
	NSMutableURLRequest *req = [[NSMutableURLRequest alloc]initWithURL:url]; [req setHTTPMethod:@"POST"];
	
	NSString *paramDataString = [NSString stringWithFormat:@"&login=%@&senha=%@",txEmail.text,txSenha.text];
	
	NSData *paramData = [paramDataString dataUsingEncoding:NSUTF8StringEncoding]; 
	[req setHTTPBody: paramData];
    [req setTimeoutInterval:10.0];

	NSURLConnection *theConnection = [[NSURLConnection alloc] initWithRequest:req delegate:self];
	
    
    if (theConnection) { 
		NSMutableData *data = [[NSMutableData alloc] init]; 
		self.receivedData = data; 
	}
    
}

//===============================================================================================================================


-(BOOL)validateEmail:(NSString *)email {
	
	NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";   
	NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];   
	return [emailTest evaluateWithObject:email];  
}

#pragma mark -
#pragma mark Memory management

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Relinquish ownership any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    // Relinquish ownership of anything that can be recreated in viewDidLoad or on demand.
    // For example: self.myOutlet = nil;
}

- (void)dealloc {
}


@end
