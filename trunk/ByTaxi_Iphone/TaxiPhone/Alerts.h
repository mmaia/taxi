//
//  Alerts.h
//  DataBase
//
//  Created by herivelto on 9/3/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Alerts : NSObject {

}

-(void) alert: title : messagem ;
-(void) alert: title : messagem : button;
-(void) alert:title :messagem :button :otherButton1;
-(void) alert:title :messagem :button :otherButton1 :otherButton2;

	

@end
