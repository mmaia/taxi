//
//  DetalheCorridaViewController.m
//  TaxiPhone
//
//  Created by Herivelto Santos on 5/16/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "DetalheCorridaViewController.h"
#import "CorridasViewController.h"
#import "Staticos.h"
#import "User.h"
#import "AppDelegate.h"

@implementation DetalheCorridaViewController

@synthesize origem,nome,telefone,informacoes;
@synthesize passageiros,idSolicitacao;
@synthesize tituloNome,tituloOrigem,tituloTelefone,tituloPassageiros,tituloMsg,tituloInformacoes;
@synthesize btFinalizar,locationTimer,locationManager,latitude,longitude,requisicao;//,url;

NSMutableData *responseData;
NSDictionary *parsedData;
NSMutableArray *cliente;

int responseStatusCode;
bool bBuscandoUsuario;

//=================================================================================================================================
// - Método constutor
//   Atualiza todos textos da tela com o texto correspondente a linguagem.
//=================================================================================================================================

- (void)viewDidLoad
{
    [super viewDidLoad];
    bBuscandoUsuario = true;

    //Remove o botão voltar da barra superior
    self.navigationItem.hidesBackButton = YES;
    
    btFinalizar.titleLabel.text = NSLocalizedString(@"BOTAO_FINALIZAR",nil);
    tituloNome.text = NSLocalizedString(@"TITULO_NOME",nil);
    tituloTelefone.text = NSLocalizedString(@"TITULO_TELEFONE",nil);
    tituloPassageiros.text = NSLocalizedString(@"TITULO_PASSAGEIROS",nil);
    tituloOrigem.text = NSLocalizedString(@"TITULO_ORIGEM",nil);
    tituloInformacoes.text = NSLocalizedString(@"TITULO_INFORMACOES",nil);
    tituloMsg.text = NSLocalizedString(@"TITULO_MENSAGEM",nil);
    
    //Inicia o GPS e atualiza a posição atual. 
    self.locationManager = [[CLLocationManager alloc]init];
	locationManager.delegate = self;
	locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    
    [self getHTTP];

    //Inicializa o Timer com o tempo e para qual método vai ser chamado.
    self.locationTimer = [NSTimer scheduledTimerWithTimeInterval:10.0 target:self selector:@selector(sendForServer) userInfo:nil repeats:YES];
    
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

//=================================================================================================================================
// - Método stopUpdatingLocations
//  Finaliza a atualização do GPS e o envio para o servidor 
//=================================================================================================================================

- (IBAction)stopUpdatingLocations{ 
    [locationManager stopUpdatingLocation]; 
    [locationTimer invalidate]; 
    [self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:1] animated:YES];	
}

//=================================================================================================================================
// - Método sendForServer
//   Envia para o server a cada tempo a posição atual para atualizar o mapa onde o taxista está
//=================================================================================================================================

- (void)sendForServer { 
    [self getHTTPPosicao];
    
}

//=================================================================================================================================
// - Método locationManager
//   Atualiza a posição de Latitude e Longitude pela posição do GPS
//=================================================================================================================================

- (void) locationManager:(CLLocationManager *) manager didUpdateToLocation:(CLLocation *) newLocation fromLocation:(CLLocation *) oldLocation {
    latitude    = [[NSString alloc] initWithFormat:@"%g", newLocation.coordinate.latitude];// @"-15.8071";
	longitude   = [[NSString alloc] initWithFormat:@"%g", newLocation.coordinate.longitude]; //@"-47.891121";
}


//=================================================================================================================================
// - Métodos HTTP que fazem as Chamadas, montam URL e trazem os métodos de Callback para retorno do HTTP
//
//=================================================================================================================================

-(void)getHTTP{
	NSURL *url = [[NSURL alloc] initWithString:[Staticos URL_RESERVAR_SOLICITACAO]]; 

	requisicao = [[NSMutableURLRequest alloc ]initWithURL:url]; 
            
    //Cria o Objeto Json 
    NSString *jsonString = [[NSString alloc] initWithFormat:@"{\"idSolicitacao\":%@,\"senhaDevice\":\"%@\"}",idSolicitacao,[User getID]];    
    NSData *paramData = [jsonString dataUsingEncoding:NSUTF8StringEncoding]; 
    [requisicao setTimeoutInterval:10.0];
    [requisicao setHTTPBody:paramData];
    [requisicao setHTTPMethod:@"PUT"];
    [requisicao setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    //Faz a requisição ao servidor
    [self doPostRequest];
    //Começa atualizar o GPS
    [locationManager startUpdatingLocation];	
}


-(void)getHTTPPosicao{
    NSURL *url = [[NSURL alloc] initWithString:[Staticos URL_ATUALIZA_POSICAO]]; 
    requisicao = nil;
    requisicao = [[NSMutableURLRequest alloc ]initWithURL:url]; 

   
    NSString *jsonString = [[NSString alloc] initWithFormat:@"{\"idSolicitacao\":%@,\"senhaDevice\":\"%@\",\"latitude\":%@,\"longitude\":%@}",idSolicitacao,[User getID],latitude,longitude];    
    
    NSData *paramData = [jsonString dataUsingEncoding:NSUTF8StringEncoding]; 
    [requisicao setTimeoutInterval:10.0];
    [requisicao setHTTPBody:paramData];
    [requisicao setHTTPMethod:@"POST"];
    [requisicao setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
	//Seta os parâmetros HTTP
	[requisicao setHTTPBody:paramData];
    [self doPostRequest];

}

-(void)doPostRequest {
	    
    NSURLConnection *connection = [[NSURLConnection alloc]initWithRequest:requisicao delegate:self];
	if (connection) { 
        NSMutableData *dData = [NSMutableData data];
        responseData = dData;
	}
    
}

#pragma mark - 
#pragma mark NSURLConnection Callbacks 

//=================================================================================================================================
// - Métodos HTTP que Recebe os dados do servidor
//
//=================================================================================================================================
-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data { 
        [responseData appendData:data];       
}

//=================================================================================================================================
// - Métodos HTTP que e chamado quando dá timeOut
//
//=================================================================================================================================
-(void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
	responseData = nil;	
} 

//=================================================================================================================================
// - Finaliza a conecção com o servidor, Faz o parser nos dados recebidos pelo json para o Dictionary parsedData
// - Cria um novo Dictionary "dadosDasCorridas" em separado para serem tratados. 
// - Adiciona cada novo objeto no NSMutableArray "allCorridas" que será usado para imprimir as colunas na tabela
// 
//=================================================================================================================================

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    if(bBuscandoUsuario){
        NSError *error;
        //Serializa o responseData JSON para parsedData
        parsedData = [NSJSONSerialization 
                      JSONObjectWithData:responseData
                      options:kNilOptions 
                      error:&error];
        
        NSString *sobreNome = [parsedData objectForKey:@"sobreNome"];    
        if (sobreNome == NULL) {
            sobreNome = @"";
        }    
        nome.text = [NSString stringWithFormat:@"%@ %@",[parsedData objectForKey:@"nome"],sobreNome];
        telefone.text = [parsedData objectForKey:@"celular"];
        bBuscandoUsuario = false;
    }  
}

//=================================================================================================================================
// - Método que recebe e trata o STATUS_CODE no response do servidor
// 
//=================================================================================================================================
- (void)connection:(NSURLConnection*)connection didReceiveResponse:(NSURLResponse*)response {
    
	NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
	responseStatusCode = [httpResponse statusCode];    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - View lifecycle

- (void)viewDidUnload
{
    [super viewDidUnload];
}
- (void)viewWillUnload
{
    [super viewWillUnload];
}


- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
