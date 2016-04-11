var chgpass = require('config/chgpass');
var register = require('config/register');
var login = require('config/login');
var requests = require('config/requests');
var editProfile = require('config/editProfile');
var reportAdd = require('config/reportAdd');
var models = require('config/models');
var locationRefresh = require('config/locationRefresh');
var fs = require('fs');
var distance = 1000/6371;


module.exports = function(app) {



	app.get('/', function(req, res) {


		res.end("Node-Android-Project"); 
	});


	app.post('/login',function(req,res){
		var phone_number = req.body.phone_number;
        	var password = req.body.password;
        	var gcm_id = req.body.gcm_id;

		login.login(phone_number,password,gcm_id,function (found) {
			console.log(found);
			res.json(found);
	});
	});


	app.post('/getContacts',function(req,res){
		var phone_number = req.body.phone_number;
        	//var password = req.body.password;

		getContacts.getContacts(phone_number,function (found) {
			console.log(found);
			res.json(found);
	});
	});


	app.post('/upload', function(req, res) {
    	console.log(req.files.image.originalFilename);
    	console.log(req.files.image.path);
        fs.readFile(req.files.image.path, function (err, data){
        	var dirname = "C:/Users/Sneh_132/Downloads/TRAVEY";
        	var newPath = dirname + "/uploads/" +   req.files.image.originalFilename;
        	fs.writeFile(newPath, data, function (err) {
        		if(err){
        			res.json({'response':"Error"});
        		}else {
        			res.json({'response':"Saved"});
				}
			});
		});
	});
 
 
	app.get('/uploads/:file', function (req, res){
        file = req.params.file;
        var dirname = "C:/Users/Sneh_132/Downloads/TRAVEY";
        var img = fs.readFileSync(dirname + "/uploads/" + file);
        res.writeHead(200, {'Content-Type': 'image/jpg' });
        res.end(img, 'binary');
 	});

	app.post('/register',function(req,res){
		//var desti = JSON.parse(req.body.history);
		var lat = req.body.lat;
		var long = req.body.long;
		var history = req.body.history;
		var user_name = req.body.user_name;
		var phone_number = req.body.phone_number;
		var email = req.body.email;
        var password = req.body.password;
        var gcm_id = req.body.gcm_id;

			
		register.register(user_name,phone_number,email,password,history,lat, long,gcm_id,function (found) {
			console.log(found);
			//console.log(desti[0].destination);
			res.json(found);
	});		
	});


	app.post('/reportAdd',function(req,res){
		//var desti = JSON.parse(req.body.history);
		//var upvotes = req.body.upvotes;
		var lat = req.body.lat;
		var long = req.body.long;
		//var downvotes = req.body.downvotes;
		var detail = req.body.detail;
		var phone_number = req.body.phone_number;
		var tag = req.body.tag;
        var start_time = req.body.start_time;
        var end_time = req.body.end_time;

			
		reportAdd.reportAdd(phone_number,detail,tag,start_time,end_time,lat,long,function (found) {
			console.log(found);
			console.log(phone_number);
			res.json(found);
	});		
	});
	

	app.post('/api/chgpass', function(req, res) {
		var id = req.body.id;
        var opass = req.body.oldpass;
		var npass = req.body.newpass;

		chgpass.cpass(id,opass,npass,function(found){
			console.log(found);
			res.json(found);
	});	
	});


	app.post('/api/resetpass', function(req, res) {
	
		var email = req.body.email;
		
		chgpass.respass_init(email,function(found){
			console.log(found);
			res.json(found);
	});		
	});
	

	app.post('/api/resetpass/chg', function(req, res) {
	
		var email = req.body.email;
		var code = req.body.code;
		var npass = req.body.newpass;
		
		chgpass.respass_chg(email,code,npass,function(found){			
			console.log(found);
			res.json(found);
	});		
	});

	app.post('/editProfile/email', function(req, res) {
	
		var phone_number = req.body.phone_number;
		var email = req.body.email;
		
		editProfile.editEmail(phone_number,email,function(found){			
			console.log(found);
			res.json(found);
	});		
	});

	app.post('/editProfile/user_name', function(req, res) {
	
		var phone_number = req.body.phone_number;
		var user_name = req.body.user_name;
		
		editProfile.editUserName(phone_number,user_name,function(found){			
			console.log(found);
			res.json(found);
	});		
	});

	app.post('/editProfile/image', function(req, res) {
	
		var phone_number = req.body.phone_number;
		var image = req.body.image;
		
		editProfile.editImage(phone_number,image,function(found){			
			console.log(found);
			res.json(found);
	});		
	});


	app.post('/ckeckExistNumber', function(req, res){
		models.User.find({phone_number: req.body.phone_number},function(err,users){

				var len = users.length;

				if(len == 0){
				 	res.json({'response':"yes"});
				}else{

					res.json({'response':"no"});
				}
			});
	});

	app.post('/editProfile/phone_number', function(req, res) {
	
		var phone_number = req.body.phone_number;
		var new_phone_number = req.body.new_phone_number;
		
		editProfile.editPhoneNumber(phone_number,new_phone_number,function(found){			
			console.log(found);
			res.json(found);
	});		
	});


	app.post('/editProfile/shared_location', function(req, res) {
	
		var phone_number = req.body.phone_number;
		var shared_location = req.body.shared_location;
		
		editProfile.editSharedLocation(phone_number,shared_location,function(found){			
			console.log(found);
			res.json(found);
	});		
	});

	app.post('/editProfile/allowed_to_post', function(req, res) {
	
		var phone_number = req.body.phone_number;
		var allowed_to_post = req.body.allowed_to_post;
		
		editProfile.editAllowedToPost(phone_number,allowed_to_post,function(found){			
			console.log(found);
			res.json(found);
	});		
	});


	app.post('/send',function(req,res) {
   		console.log("hi");
   		var fromu = req.body.from;
   		var fromn = req.body.fromn;
   		var to = req.body.to;
   		var title = req.body.title;


   		requests.send(fromn, fromu, title,to, function (found) {
      		console.log("hii");
      		res.json(found);
      	});
	});

	app.post('/historyAdd',function(req,res){
		var phone_number = req.body.phone_number;
		var history = req.body.history;

		historyAdd.historyAdd(phone_number,history,function (found) {
			console.log(found);
			//res.json(found);
		});
	});

	app.post('/locationRefresh',function(req,res){
		var phone_number = req.body.phone_number;
		var lat = req.body.lat;
		var long=req.body.long;

		locationRefresh.locationRefresh(phone_number,lat,long,function (found) {
			console.log(found);
			res.json(found);
		});
	});



	app.post('/allReports',function(req,res){
   		models.Report.find({},function(err,reports){
     	console.log(reports);

      	res.json(reports);
   	});
	});


	app.post('/setGcmid',function(req,res){
   		models.User.find({phone_number: req.body.phone_number},function(err,reports){
   			reports[0].gcm_id = req.body.gcm_id;
   			reports[0].save();
     		console.log(reports);

      		res.json(reports);
   		});
	});



	app.post('/allContacts',function(req,res){
   		models.User.find({},function(err,reports){
     	console.log(reports);

      	res.json(reports);
   	});
	});

app.post('/reportNearBy',function(req,res){
   var query = models.Report.find({'location': {
      $near: [req.body.lat,req.body.long],
      $maxDistance: distance
   }
   });

   query.exec(function (err, city) {
      if (err) {
         console.log(err);
         throw err;
      }
      else {
         console.log('hi '+ city);
         res.json(city);
      }

   });

});


app.post('/friendsNearBy',function(req,res){
   var query = models.User.find({'location': {
      $near: [req.body.lat,req.body.long],
      $maxDistance: distance
   }
   });

   query.exec(function (err, city) {
      if (err) {
         console.log(err+ "what is this");
         throw err;
      }
      else {
         console.log('hi this is friends near by '+ city);
         res.json(city);
      }

   });

});



app.post('/getLocation',function(req,res){
	models.User.find({'phone_number': req.body.phone_number}, function(error,reports){
		if(reports[0].shared_location == "1"){
			res.json(reports[0].location);
		}else{
			var fromu = "req.body.phone_number";
   			var fromn = "req.body.fromn";
   						
			var title = "Share your location";

			requests.send(fromn, fromu, title,reports[0].gcm_id, function (found) {
      			console.log("location send");
      			res.json(found);
      		});	
		}
		res.json();
	});
});


app.post('/shareLocation',function(req,res){
	models.User.find({'phone_number': req.body.phone_number}, function(error,reports){
		var fromu = reports[0].user_name;
   		var fromn = "req.body.fromn";
   						
		var title = phone_number;

		requests.send(fromn, fromu, title,reports[0].gcm_id, function (found) {
      		console.log("location send");
      		res.json(found);
      	});
		//res.json(reports[0].location);
	});
});


app.post('/reportUpVote',function(req,res){
   models.Report.find({_id:req.body.reportId},function(error, reports){
      reports[0].upvotes=reports[0].upvotes+1;
      reports[0].save(function(err,resp) {
        if(err) {
            console.log(err);
            
        } else {
            console.log("done");
        }
    });
      var phone_number=reports[0].phone_number;
      models.User.find({phone_number:phone_number},function(err,users){
      	console.log(users[0]);
         var up=users[0].upvotes;
         users[0].upvotes=up+1;
         users[0].save(function(err,resp) {
        if(err) {
            console.log(err);
            
        } else {
            console.log("done");
        }
    });
      });
   });
   res.json({'response' : "upvote ho gya"});
});
app.post('/reportDownVote',function(req,res){
   models.Report.find({_id:req.body.reportId},function(error, reports){
      reports[0].downvotes=reports[0].downvotes+1;
      reports[0].save(function(err,resp) {
        if(err) {
            console.log(err);
            
        } else {
            console.log("done");
        }
    });
      var phone_number=reports[0].phone_number;
      models.User.find({phone_number:phone_number},function(err,users){
                var down=users[0].downvotes;
  				users[0].downvotes=down+1;
         if(users[0].upvotes - users[0].downvotes < -10){
         	users[0].allowed_to_post = "0";

         	var fromu = "req.body.phone_number";
   			var fromn = "req.body.fromn";
   						
			var title = "Now you are not allowed to post";

			requests.send(fromn, fromu, title,users[0].gcm_id, function (found) {
      			console.log(users[0].phone_number + "  not allowed to post");
      			res.json(found);
      		});	
         }
         	
         users[0].save();
      });
   });
   res.json({'response': "downvote ho gya"});
});

app.post('/leaderBoard',function(req,res) {
   var options = {
      "limit": 20,
      "sort": [['upvotes', 'desc'], ['downvotes', 'asc']]
   };

   models.User.find({}, function (err, docs) {
      console.log("retrieved records:");
      console.log(docs);
      res.json(docs);
   });
});

app.post('/history', function(req,res){
	models.User.find({phone_number : req.body.phone_number}, function(err, users){
			res.json(users[0]);
	});
});


app.post('/getImage', function(req, res){
	models.User.find({phone_number : req.body.phone_number}, function(err, users){
			res.json(users[0].image);
			//console.log(users[0].image);
	});
});

app.post('/shareLocationWithFriend', function(req, res){
	models.User.find({phone_number : req.body.phone_number}, function(err, users){
		var fromu = "req.body.phone_number";
   		var fromn = "req.body.fromn";
   						
   		var title = "Share your location to"+ req.body.user_name;

			requests.send(fromn, fromu, title,users[0].gcm_id, function (found) {
      			console.log(users[0].phone_number );
      			console.log(users[0].gcm_id);
      			console.log(found);
      			res.json(found);
      		});	
			//res.json(users[0].image);
			//console.log(users[0].image);
	});
});

	
};



