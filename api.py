from flask import *
from database import *

# import demjson
import uuid

api=Blueprint('api',__name__)

@api.route('/logins',methods=['post'])
def login():
    data={}
    
    username = request.form['username']
    password = request.form['password']
    q="SELECT * from login where username='%s' and password='%s'" % (username,password)
    res = select(q)
    print(res)
    if res :
        if res[0]['usertype']=="user":
            return jsonify(status="ok",lid=res[0]["login_id"])
        else:
            return jsonify(status="no")
    else:
        return jsonify(status="no")



@api.route('/user_reg',methods=['post'])
def user_reg():
    fname=request.form['fname']
    lname=request.form['lname']
    place=request.form['place']
    phone=request.form['phone']
    email=request.form['email']
    uname=request.form['uname']
    pwd=request.form['pwd']
    y="insert into login values(null,'%s','%s','user')"%(uname,pwd)
    log=insert(y)
    r="insert into user values(null,'%s','%s','%s','%s','%s','%s')"%(log,fname,lname,place,phone,email)
    res=insert(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/add_skill',methods=['post'])
def add_skill():
    
    login_id=request.form['login_id']
    e="select * from user where login_id='%s'"%(login_id)
    res=select(e)
    if res:
        user_id=res[0]['user_id']
        print("mmmmmmmmmmmm",user_id)
    cid=request.form['cid']
    print("iiiiiiiiiiiii",cid)
    sid=request.form['sid']
    print("iiiiiiiiiiiii",sid)
    r="insert into myskills values(null,'%s','%s','%s')"%(user_id,cid,sid)
    res=insert(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_view_other_user',methods=['post'])
def user_view_other_user():
    data={}
    login_id=request.form['login_id']
    print("jjjjjjjjjjjjjjjjjjjjjj",login_id)
    r="select * from user where not login_id='%s'"%(login_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")



@api.route('/viewcate',methods=['post'])
def viewcate():
    data={}
    
    r="select * from badgeorchive"
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")



@api.route('/viewskill',methods=['post'])
def viewskill():
    data={}
    
    r="select * from skills"
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/viewskillandcate',methods=['post'])
def viewskillandcate():
    data={}
    
    r="select * from myskills"
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_view_skill_cate',methods=['post'])
def user_view_skill_cate():
    data={}
    userid=request.form['user_id']
    print("jjjjjjjjjjjjjjj",userid)
    
    r="select * from myskills inner join badgeorchive using(badgeorchive_id) inner join skills using(skills_id) where user_id='%s'"%(userid)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_sendrequest',methods=['post'])
def user_sendrequest():
    details=request.form['details']
    login_id=request.form['login_id']
    mysk=request.form['mysk']
    y="insert into request values(null,'%s',(select user_id from user where login_id='%s'),'%s','pending',curdate(),'pending')"%(mysk,login_id,details)
    res=insert(y)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_add_project',methods=['post'])
def user_add_project():
    project=request.form['project']
    details=request.form['details']
    amount=request.form['amount']
    login_id=request.form['login_id']
    t="select * from user where login_id='%s'"%(login_id)
    re2=select(t)
    if re2:
        user_id=re2[0]['user_id']

    y="insert into projects values(null,'%s','%s','%s',curdate(),'%s','Available')"%(user_id,project,details,amount)
    res=insert(y)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/user_view_project',methods=['post'])
def user_view_project():
    data={}
    login_id=request.form['login_id']
    y="select * from user where login_id='%s'"%(login_id)
    re2=select(y)
    if re2:
        user_id=re2[0]['user_id']
    r="select * from projects where user_id='%s'"%(user_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_view_other_project',methods=['post'])
def user_view_other_project():
    data={}
    login_id=request.form['login_id']
    y="select * from user where login_id='%s'"%(login_id)
    re2=select(y)
    if re2:
        user_id=re2[0]['user_id']
    r="select * from projects where not user_id='%s'"%(user_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_add_highlights',methods=['post'])
def user_add_highlights():
    highlights=request.form['highlights']
    image=request.files['pic']
    path="static/images/"+str(uuid.uuid4())+image.filename
    image.save(path)
    proid=request.form['proid']
    
    

    y="insert into highlights values(null,'%s','%s','%s')"%(proid,highlights,path)
    res=insert(y)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_view_highlight',methods=['post'])
def user_view_highlight():
    data={} 
    pro_id=request.form['pro_id']
    r="select * from highlights where project_id='%s'"%(pro_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/prorequest',methods=['post'])
def prorequest():
    data={}
    
    login_id=request.form['login_id']
    pro_id=request.form['pro_id']
    print("oooooooooooo",login_id)
    r = "INSERT INTO prequest VALUES (NULL, (SELECT user_id FROM user WHERE login_id='%s'), '%s', CURDATE(), 'pending')" % (login_id, pro_id)
    res=insert(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/user_send_comp',methods=['post'])
def user_send_comp():
    data={}
    login_id=request.form['login_id']
    
    comp=request.form['comp']
    t="select * from user where login_id='%s'"%(login_id)
    res3=select(t)
    if res3:
        user_id=res3[0]['user_id']

    r="insert into complaint values(null,'%s','%s','pending',curdate())"%(user_id,comp)
    res=insert(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_view_comp',methods=['post'])
def user_view_comp():
    data={} 
    login_id=request.form['login_id']
    user_id=request.form['user_id']
    r="select * from complaint where user_id=(select user_id from user where login_id='%s')"%(login_id)
    res=select(r)
    print(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/View_Requested_Projects',methods=['post'])
def View_Requested_Projects():
    data={} 
    login_id=request.form['login_id']
    user_id=request.form['user_id']
    
    r="select *,prequest.status as stat from prequest inner join projects on projects.project_id=prequest.project_id where prequest.user_id=(SELECT user_id FROM user WHERE login_id='%s')"%(login_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/view_other_request',methods=['post'])
def view_other_request():
    data={} 
    login_id=request.form['login_id']
    # user_id=request.form['user_id']
    
    r="select *,prequest.status as stat from prequest inner join projects on projects.project_id=prequest.project_id where projects.user_id=(SELECT user_id FROM user WHERE login_id='%s')"%(login_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_accept_req',methods=['post'])
def user_accept_req ():
    data={} 
    pro_id=request.form['pro_id']
    login_id=request.form['login_id']
    req_id=request.form['req_id']
    print('kkkkkkkkkkkkkkk',login_id)
    u="update prequest set status='accept' where project_id='%s' and prequest_id='%s'"%(pro_id,req_id)
    res=update(u)
    if res :

        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_reject_req',methods=['post'])
def user_reject_req ():
    data={} 
    pro_id=request.form['pro_id']
    login_id=request.form['login_id']
    req_id=request.form['req_id']
    
    u="update prequest set status='reject' where project_id='%s' and prequest_id='%s'"%(pro_id,req_id)
    res=update(u)
    if res :

        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/user_view_payment_1',methods=['post'])
def user_view_payment_1():
    data={} 
    login_id=request.form['login_id']
    req_id=request.form['req_id']
    r="select * from payment where request_id='%s'"%(req_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_add_issue',methods=['post'])
def user_add_issue():
    data={} 
    issue=request.form['issue']
    image=request.files['pic']
    path="static/images/"+str(uuid.uuid4())+image.filename
    image.save(path)
    req_id=request.form['req_id']
    r="insert into issues values(null,'%s','%s','%s')"%(req_id,issue,path)
    res=insert(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/user_view_issue',methods=['post'])
def user_view_issue():
    data={} 
    login_id=request.form['login_id']
    req_id=request.form['req_id']
    r="select * from issues where prequest_id='%s'"%(req_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/user_make_payment_1',methods=['post'])
def user_make_payment_1():
    data={} 
    pre_id=request.form['pre_id']
    amount=request.form['amount']
   
    r="insert into payment values(null,'%s','projects','%s',curdate())"%(pre_id,amount)
    res=insert(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/user_search_user',methods=['post'])
def user_search_user():
    data={} 
    r="select * from user inner join myskills using(user_id) inner join badgeorchive using(badgeorchive_id) inner join skills using(skills_id)"
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")




@api.route('/user_search_sendrequest',methods=['post'])
def user_search_sendrequest():
    details=request.form['details']
    login_id=request.form['login_id']
    mysk=request.form['mysk']
    y="insert into request values(null,'%s',(select user_id from user where login_id='%s'),'pending','%s',curdate(),'pending')"%(mysk,login_id,details)
    res=insert(y)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/search_skill',methods=['post'])
def search_skill():
    data={} 
    s=request.form['skillorbadge']
    r="select * from user inner join myskills using(user_id) inner join badgeorchive using(badgeorchive_id) inner join skills using(skills_id) where skills like '%s' or badgeorchive like '%s'"%(s,s)
    res=select(r)
    
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/pro_unavailable',methods=['post'])
def pro_unavailable():
    project_id=request.form['project_id']
    y="update projects set status='unavailable' where project_id='%s'"%(project_id)
    res=update(y)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/pro_available',methods=['post'])
def pro_available():
    project_id=request.form['project_id']
    y="update projects set status='Available' where project_id='%s'"%(project_id)
    res=update(y)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")



@api.route('/user_view_skill_badgeorachive',methods=['post'])
def user_view_skill_badgeorachive():
    data={}
    login_id=request.form['login_id']
    
    r="select * from myskills inner join badgeorchive using(badgeorchive_id) inner join skills using(skills_id) where user_id=(select user_id from user where login_id='%s')"%(login_id)
    res=select(r)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")
@api.route('/deleteskill',methods=['post'])
def deleteskill():
    
    myid=request.form['myid']
    
    r="delete from myskills where myskills_id='%s'"%(myid)
    res=delete(r)
    print("pppppppppppppppp",res)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")

@api.route('/deleteproject',methods=['post'])
def deleteproject():
    
    proid=request.form['pro_id']
    t="delete from highlights where project_id='%s'"%(proid)
    delete(t)
    r="delete from projects where project_id='%s'"%(proid)
    res=delete(r)

    
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")




# ++++++++++++++++++++++\


@api.route('/user_view_request',methods=['post'])
def user_view_request():
    data={}
    login_id = request.form['login_id']
    # q="SELECT * FROM `request` INNER JOIN `user` USING(user_id) INNER JOIN `myskills` USING (myskills_id)INNER JOIN `skills` ON `skills`.`skills_id` =`myskills`.`skill_id` where `user`.user_id=(select user_id from user where login_id<>'%s') "%(login_id)
    q="""SELECT r.request_id,r.myskills_id,s.skills,u2.`user_id`,u1.`login_id`,u2.phone,r.amount,r.details,r.date,r.status,u1.ufname,u1.ulname 
FROM request r,USER u1,USER u2,myskills m,skills s 
WHERE r.user_id=u1.user_id AND r.`myskills_id`=m.`myskills_id`AND m.`skills_id`=s.`skills_id` 
AND m.user_id=u2.user_id AND  r.user_id IN (SELECT user_id FROM USER WHERE login_id != '%s')"""  %(login_id)
    print(q)
    res=select(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")


@api.route('/user_send_amts',methods=['post'])
def user_send_amts():
    amtss=request.form['amtss']    
    detss=request.form['detss']    
    req_id=request.form['req_id']
    q="update `request` set amount='%s',details='%s',status='amount_sended' where request_id='%s'"%(amtss,detss,req_id)
    res=update(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")





@api.route('/user_view_sended_request',methods=['post'])
def user_view_sended_request():
    data={}
    login_id = request.form['login_id']
    # q="SELECT * FROM `request` INNER JOIN `user` USING(user_id) INNER JOIN `myskills` USING (myskills_id)INNER JOIN `skills` ON `skills`.`skills_id` =`myskills`.`skill_id` where `user`.user_id=(select user_id from user where login_id='%s') "%(login_id)
    q="SELECT r.request_id,r.myskills_id,s.skills,u2.`user_id`,u2.`login_id`,u2.phone,r.amount,r.details,r.date,r.status,u2.ufname,u2.ulname FROM request r,USER u1,USER u2,myskills m,skills s WHERE r.user_id=u1.user_id AND r.`myskills_id`=m.`myskills_id` AND m.`skills_id`=s.`skills_id` AND m.user_id=u2.user_id and  r.user_id=(select user_id from user where login_id='%s')"  %(login_id)
    print(q)
    res=select(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no") 



@api.route('/user_make_payment',methods=['post'])
def user_make_payment():
    amtss=request.form['amts']    
    req_id=request.form['req_id']
    q="update `request` set status='paid' where request_id='%s'"%(req_id)
    print(q)
    update(q)    
    q="insert into `payment` values(null,'%s','skill','%s',curdate())"%(req_id,amtss)
    print(q)
    res=insert(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")





@api.route('/user_sended_chat',methods=['post'])
def user_sended_chat():
    sender_id=request.form['sender_id']    
    receiver_id=request.form['receiver_id']
    chat=request.form['details']
  
    q="insert into `chat` values(null,'%s','%s','%s',curdate())"%(sender_id,receiver_id,chat)
    print(q)
    res=insert(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")





@api.route('/user_view_sended_chat',methods=['post'])
def user_view_sended_chat():
    sender_id=request.form['sender_id']    
    receiver_id=request.form['receiver_id']
    q="SELECT * FROM `chat` where (sender_id='%s' and receiver_id='%s') or (sender_id='%s' and receiver_id='%s') "%(sender_id,receiver_id,receiver_id,sender_id)
    print(q)

    res=select(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")



@api.route('/usersend_view_request_chat',methods=['post'])
def usersend_view_request_chat():
    sender_id=request.form['sender_id']    
    receiver_id=request.form['receiver_id']
    chat=request.form['details']
  
    q="insert into `chat` values(null,'%s','%s','%s',curdate())"%(sender_id,receiver_id,chat)
    print(q)
    res=insert(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")







@api.route('/user_view_request_chat',methods=['post'])
def user_view_request_chat():
    sender_id=request.form['sender_id']    
    receiver_id=request.form['receiver_id']
    q="SELECT * FROM `chat` where (sender_id='%s' and receiver_id='%s') or (sender_id='%s' and receiver_id='%s') "%(sender_id,receiver_id,receiver_id,sender_id)
    print(q)

    res=select(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")




@api.route('/user_view_payment',methods=['post'])
def user_view_payment():
    req_id=request.form['req_id']    
    q="SELECT * FROM `payment` inner join request using(request_id) inner join user using (user_id) where request_id='%s' "%(req_id)
    print(q)

    res=select(q)
    if res :
        return jsonify(status="ok",data=res)
    else:
        return jsonify(status="no")