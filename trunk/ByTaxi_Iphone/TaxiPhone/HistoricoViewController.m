//
//  HistoricoViewController.m
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/19/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "HistoricoViewController.h"
#import "PopUp.h"
#import "Staticos.h"
#import "User.h"
@implementation HistoricoViewController



int responseStatusCode ;
NSMutableData *responseData;
NSDictionary *parsedData;
NSMutableURLRequest *requisicao;


@synthesize allCorridas;


//=================================================================================================================================
// - Construtor
//   Faz a chamada ao "doPostRequest" para acessar o servidor e buscar as corridas em aberto. 
//=================================================================================================================================

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    self.title = NSLocalizedString(@"TITULO_HISTORICO",nil);
    
    [self getHTTP];
    
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

//=================================================================================================================================
// - getHTTP 
//   Monta o HTTP com os parâmentros do JSon no corpo do HTTP
//=================================================================================================================================

-(void)getHTTP {
    
	NSURL *url = [[NSURL alloc] initWithString:[Staticos URL_HISTORICO]]; 
	
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


- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [allCorridas count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        //Configura as celulas da TableView
        //cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;    
        cell.textLabel.adjustsFontSizeToFitWidth = YES;
        cell.textLabel.font = [UIFont systemFontOfSize:12];
        cell.textLabel.numberOfLines = 3;
        cell.textLabel.lineBreakMode = UILineBreakModeWordWrap;
        //cell.imageView.image = [UIImage imageNamed:@"icoList.png"];     
    }
    
    //Se contiver dados no Array imprime
    if ([allCorridas count] > 0) {
        NSDictionary *dictionary = [allCorridas objectAtIndex:indexPath.section];
        cell.textLabel.text = [dictionary objectForKey:@"Origens"];
    }

    return cell;
}



- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}


#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

@end
