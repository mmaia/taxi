//
//  DetalheCorridaViewController.h
//  TaxiPhone
//
//  Created by Herivelto Santos on 5/16/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@class CorridasViewController;
@interface DetalheCorridaViewController : UIViewController <CLLocationManagerDelegate>{
    
    UILabel *origem;
    UILabel *nome;
    UILabel *telefone;
    UILabel *passageiros;
    UILabel *informacoes;
    NSString *idSolicitacao;

    UILabel *tituloOrigem;
    UILabel *tituloNome;
    UILabel *tituloTelefone;
    UILabel *tituloPassageiros;
    UILabel *tituloInformacoes;
    UILabel *tituloMsg;
    
    UIButton *btFinalizar;
    NSTimer *localtionTimer;
    CLLocationManager *locationManager;
	NSString *latitude;
	NSString *longitude;
    NSMutableURLRequest *requisicao;
   // NSURL *url;
}
//@property (nonatomic, retain) NSURL *url;
@property (nonatomic, retain) NSMutableURLRequest *requisicao;
@property (retain, nonatomic ) NSString *latitude;
@property (retain, nonatomic ) NSString *longitude;
@property (retain, nonatomic) CLLocationManager *locationManager;
@property (strong, nonatomic) NSTimer *locationTimer;
@property (nonatomic, retain) IBOutlet UILabel *origem;
@property (nonatomic, retain) IBOutlet UILabel *nome;
@property (nonatomic, retain) IBOutlet UILabel *telefone;
@property (nonatomic, retain) IBOutlet UILabel *passageiros;
@property (nonatomic, retain) IBOutlet UILabel *informacoes;
@property (nonatomic, retain) NSString *idSolicitacao;
@property (nonatomic, retain) IBOutlet UILabel *tituloOrigem;
@property (nonatomic, retain) IBOutlet UILabel *tituloNome;
@property (nonatomic, retain) IBOutlet UILabel *tituloTelefone;
@property (nonatomic, retain) IBOutlet UILabel *tituloPassageiros;
@property (nonatomic, retain) IBOutlet UILabel *tituloInformacoes;
@property (nonatomic, retain) IBOutlet UILabel *tituloMsg;
@property (nonatomic, retain) IBOutlet UIButton *btFinalizar;



//- (void)handleBack:(id)sender;
-(void)doPostRequest;
-(IBAction)stopUpdatingLocations;
-(void) getHTTP;
-(void)getHTTPPosicao;


@end
