//
//  CorridasViewController.m
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/19/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "CorridasViewController.h"
#import "Alerts.h"
#import "Staticos.h"
#import "User.h"
#import "DetalheCorridaViewController.h"
#import "PopUp.h"


@implementation CorridasViewController

@synthesize allCorridas,locationTimer;

static int BOTAO_ACEITAR = 0;
int responseStatusCode ;
NSDictionary *dictionarioCorridas; 
NSDictionary *parsedData;
NSNumber *idSolicitacao;
NSMutableURLRequest *requisicao;

DetalheCorridaViewController *dvController;
//=================================================================================================================================
// - Construtor
//   Faz a chamada ao "doPostRequest" para acessar o servidor e buscar as corridas em aberto. 
//=================================================================================================================================

- (void)viewDidLoad
{

    [super viewDidLoad];
    self.title = NSLocalizedString(@"TITULO_CORRIDAS",nil);

    [self getHTTP];

    //Inicializa o Timer com o tempo e para qual método vai ser chamado.
    self.locationTimer = [NSTimer scheduledTimerWithTimeInterval:30.0 target:self selector:@selector(doPostRequest) userInfo:nil repeats:YES];

}

//=================================================================================================================================
// - Métodos que definem o tamanho das linhas da tablevView
//
//=================================================================================================================================

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return  [allCorridas count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

//=================================================================================================================================
// - getHTTP 
//   Monta o HTTP com os parâmentros do JSon no corpo do HTTP
//=================================================================================================================================

-(void)getHTTP {
    
	NSURL *url = [[NSURL alloc] initWithString:[Staticos URL_SOLICITACOES_PENDENTES]]; 
	
	requisicao = [[NSMutableURLRequest alloc ]initWithURL:url]; 
    
    //Pega a senha do device
    NSString *senhaCodificada = [User getID];
    
    //Cria o Objeto Json 
    NSString *jsonString = [[NSString alloc] initWithFormat:@"{\"senhaDevice\":\"%@\"}",senhaCodificada];
    NSData *paramData = [jsonString dataUsingEncoding:NSUTF8StringEncoding]; 
    
	//Seta os parâmetros HTTP
	[requisicao setTimeoutInterval:10.0];
	[requisicao setHTTPMethod:@"POST"];
	[requisicao setHTTPBody:paramData];
	[requisicao setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        
}

//=================================================================================================================================
// - doPostRequest 
//  Faz a chamada ao servidor passando a requisição com parametro
//=================================================================================================================================

-(void)doPostRequest {
    [PopUp starPopUp:NSLocalizedString(@"Atualizando Corridas \n Aguarde...",nil)];
    allCorridas = [[NSMutableArray alloc] init];
        
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
	[PopUp stopPopUp];

    NSError *error;
    //Serializa o responseData JSON para parsedData
    parsedData = [NSJSONSerialization 
                                JSONObjectWithData:responseData
                                options:kNilOptions 
                                error:&error];
      
    //Transfor o JSOn para um Dictionary com os objetos recebidos.
    for (NSDictionary *status in parsedData){
          NSDictionary *dadosDasCorridas = [NSDictionary dictionaryWithObjectsAndKeys:
                                           [status objectForKey:@"dataHora"],@"Data", 
                                           [status objectForKey:@"destino"],@"Destinos",
                                           [status objectForKey:@"idSolicitacao"],@"id",
                                           [status objectForKey:@"informacoesAdicionais"],@"Informacoes",
                                           [status objectForKey:@"numeroPassageiros"],@"Passageiros",
                                           [status objectForKey:@"origem"],@"Origens",nil ];
        
        //Adiciona os objetos a um Array para imprimir
        [allCorridas addObject:dadosDasCorridas];
    }    
        //Recarrega a tela para atualizar os dados recebidos.
        [self.tableView reloadData];
    
}

//=================================================================================================================================
// - Método que recebe e trata o STATUS_CODE no response do servidor
// 
//=================================================================================================================================
- (void)connection:(NSURLConnection*)connection didReceiveResponse:(NSURLResponse*)response {
	NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
	responseStatusCode = [httpResponse statusCode];    
}

//=================================================================================================================================
// - Configura e Cria cada coluna da Tabela
// 
//=================================================================================================================================

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        //Configura as celulas da TableView
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;    
        cell.textLabel.adjustsFontSizeToFitWidth = YES;
        cell.textLabel.font = [UIFont systemFontOfSize:12];
        cell.textLabel.numberOfLines = 3;
        cell.textLabel.lineBreakMode = UILineBreakModeWordWrap;
        cell.imageView.image = [UIImage imageNamed:@"icoList.png"];     
    }

    //Se contiver dados no Array imprime
    if ([allCorridas count] > 0) {
        NSDictionary *dictionary = [allCorridas objectAtIndex:indexPath.section];
        cell.textLabel.text = [dictionary objectForKey:@"Origens"];
    }
    
    return cell;
}

//=================================================================================================================================
// - PopUp para aceitar corrida
//
//=================================================================================================================================

-(IBAction)showActionSheet{
    
    UIActionSheet *popupQuery = [[UIActionSheet alloc] initWithTitle:NSLocalizedString(@"TITULO_POUP_ACEITAR_CORRIDA",nil)
                                     delegate:self cancelButtonTitle:NSLocalizedString(@"BOTAO_CANCELAR_CORRIDA",nil) 
                                              destructiveButtonTitle:NSLocalizedString(@"BOTAO_ACEITAR_CORRIDA",nil)
                                                   otherButtonTitles:nil];
    
    popupQuery.actionSheetStyle = UIActionSheetStyleBlackOpaque;
    [popupQuery showInView:self.view];
    
}
//=================================================================================================================================
// - Controla qual botão foi apertado no PoupUP
// BOTAO_ACEITAR = 0  CANCELAR = 1 
//=================================================================================================================================

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    // Ser for aceita a corrida entra para a próxima tela
    if(buttonIndex == BOTAO_ACEITAR){
    
         dvController = [[DetalheCorridaViewController alloc] initWithNibName:@"DetalheCorridaViewController" bundle:[NSBundle mainBundle]];
        
        NSNumber *qtdPassageiros      = [dictionarioCorridas objectForKey:@"Passageiros"];
        
        NSString *id = [dictionarioCorridas objectForKey:@"id"];
        
        dvController.idSolicitacao = id;
        
        //@"11";//[NSString stringWithFormat:@"%d", idSolicitacao.intValue ];//[dictionarioCorridas objectForKey:@"id"];

        [self.navigationController pushViewController:dvController animated:YES];
        
        //Atualiza a próxima tela com os dados 

        dvController.passageiros.text = [NSString stringWithFormat:@"%d", qtdPassageiros.intValue ];
        dvController.origem.text      = [dictionarioCorridas objectForKey:@"Origens"];
        dvController.informacoes.text = [dictionarioCorridas objectForKey:@"Informacoes"];

    }
}

//=================================================================================================================================
// - Método que dispara quando uma linha da coluna e clicado, passa o index da coluna
//   Marca no dicictionary qual a corrida está sendo clicada. 
//
//=================================================================================================================================

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    dictionarioCorridas = [allCorridas objectAtIndex:indexPath.section];
    [self showActionSheet];    
    
}

//=================================================================================================================================
//
//
//=================================================================================================================================

- (void)viewDidUnload
{
    [super viewDidUnload];
     dvController = nil;
    [locationTimer invalidate]; 

}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    
}


- (void)viewWillAppear:(BOOL)animated
{    
    [self doPostRequest];
    if ( ! self.locationTimer.isValid) {
        self.locationTimer = [NSTimer scheduledTimerWithTimeInterval:30.0 target:self selector:@selector(doPostRequest) userInfo:nil repeats:YES];
    }    

}

- (void)viewDidAppear:(BOOL)animated
{

}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [locationTimer invalidate]; 
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [self.tableView reloadData];
}


- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end

