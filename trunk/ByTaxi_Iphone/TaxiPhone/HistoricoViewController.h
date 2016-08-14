//
//  HistoricoViewController.h
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/19/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HistoricoViewController : UITableViewController{
    
    NSMutableArray *allCorridas;

}

@property (nonatomic, retain) NSMutableArray *allCorridas;

-(void)doPostRequest;
-(void)getHTTP;

@end
