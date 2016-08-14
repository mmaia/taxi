//
//  RootViewController.h
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/15/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LoginViewController;
@class CorridasViewController;
@class HistoricoViewController;
@class AjudaViewController;

@interface RootViewController : UITableViewController{
    
    NSArray *aplicativos;
    NSArray *icones;
    NSArray *aplicativos2;
    NSArray *icones2;
    LoginViewController *loginView;
    CorridasViewController *corridasView;
    HistoricoViewController *historicoView;
    AjudaViewController *ajudaView;

}

@property (nonatomic,retain) NSArray *aplicativos1;
@property (nonatomic,retain) NSArray *icones1;
@property (nonatomic,retain) NSArray *aplicativos2;
@property (nonatomic,retain) NSArray *icones2;
@property (nonatomic,retain) LoginViewController *loginView;
@property (nonatomic,retain) CorridasViewController *corridasView;
@property (nonatomic,retain) HistoricoViewController *historicoView;
@property (nonatomic,retain) AjudaViewController *ajudaView;

@end
