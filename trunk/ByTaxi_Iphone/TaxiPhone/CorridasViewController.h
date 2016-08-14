//
//  CorridasViewController.h
//  TaxiPhone
//
//  Created by Herivelto Gabriel on 3/19/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CorridasViewController : UITableViewController<UIActionSheetDelegate>{
    
    NSMutableData *responseData;
    NSMutableArray *allCorridas;
    NSTimer *locationTimer;
}

@property (nonatomic, retain) NSMutableArray *allCorridas;
@property (strong,nonatomic) NSTimer *locationTimer;
-(void)getHTTP;
-(IBAction)showActionSheet;
-(void)doPostRequest;


@end
