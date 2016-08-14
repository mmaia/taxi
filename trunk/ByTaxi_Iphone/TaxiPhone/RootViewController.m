//
//  RootViewController.m
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/15/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "RootViewController.h"
#import "HistoricoViewController.h"
#import "AjudaViewController.h"
#import "LoginViewController.h"
#import "CorridasViewController.h"
#import "User.h"
#import "Alerts.h"

@implementation RootViewController
@synthesize aplicativos1,icones1,aplicativos2,icones2;
@synthesize loginView,corridasView,historicoView,ajudaView;



#pragma mark - View lifecycle

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    aplicativos = [[NSArray alloc] initWithObjects: NSLocalizedString(@"CORRIDAS",nil), NSLocalizedString(@"HISTORICO",nil),NSLocalizedString(@"LOGIN",nil),NSLocalizedString(@"AJUDA",nil),nil];
    
    icones = [[NSArray alloc] initWithObjects:@"btCorridas",@"btHistorico",@"btLogin",@"btAjuda", nil ];	
    
}


#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
        switch (indexPath.row) {
            case 0://Corridas
                if([User isLogado]){
                    if (self.corridasView == nil) {
                        CorridasViewController *d = [[CorridasViewController alloc] initWithNibName:@"CorridasViewController" bundle:[NSBundle mainBundle]];						
                        self.corridasView = d; 
                        [self.navigationController pushViewController:self.corridasView animated:YES];
                    }
                    corridasView = nil;
                }else{
                    [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                     NSLocalizedString(@"ALERTA_USUARIO_NAO_LOGADO",nil):
                     NSLocalizedString(@"OK",nil)]; 
                }
                break;
            case 1://Histórico
                if([User isLogado]){

                    if (self.historicoView == nil) {
                        HistoricoViewController *d = [[HistoricoViewController alloc] initWithNibName:@"HistoricoViewController" bundle:[NSBundle mainBundle]];
                        self.historicoView = d; 
                        [self.navigationController pushViewController:self.historicoView animated:YES];
                    }
                    historicoView = nil;
                }else{
                    [[Alerts alloc] alert:NSLocalizedString(@"ALERTA",nil):
                     NSLocalizedString(@"ALERTA_USUARIO_NAO_LOGADO",nil):
                     NSLocalizedString(@"OK",nil)]; 
                }
                break;                    
            case 2:
                if (self.loginView == nil) {
					LoginViewController *d = [[LoginViewController alloc] initWithNibName:@"LoginViewController" bundle:[NSBundle mainBundle]];
					self.loginView = d; 
					[self.navigationController pushViewController:self.loginView animated:YES];
				}
				loginView = nil;
            break;    
            case 3:
                if (self.ajudaView == nil) {
					AjudaViewController *d = [[AjudaViewController alloc] initWithNibName:@"AjudaViewController" bundle:[NSBundle mainBundle]];
					self.ajudaView = d; 
					[self.navigationController pushViewController:self.ajudaView animated:YES];
				}
				ajudaView = nil;
            break;    
        }
				
}

//Cell title
- (NSString *)tableView:(UITableView *)tblView titleForHeaderInSection:(NSInteger)section {
		
	return nil;
}


- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}


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
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return [aplicativos count ];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    // Configure the cell...
   NSString *imageName = nil;
			cell.textLabel.text =  [aplicativos objectAtIndex:indexPath.row];
			imageName = [NSString stringWithFormat:@"%@.png",[icones objectAtIndex:indexPath.row]];

	// Adicionar Imagem na celula
	UIImage *image = [UIImage imageNamed:imageName]; 
	cell.imageView.image = image;
	cell.accessoryType = UITableViewCellAccessoryDetailDisclosureButton;	
    return cell;
}



@end
