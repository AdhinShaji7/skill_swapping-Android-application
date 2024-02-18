from flask import *
from database import *
admin=Blueprint('admin',__name__)

@admin.route('/adminhome')
def adminhome():
	return render_template('adminhome.html')

@admin.route('/admin_addskills',methods=['get','post'])
def admin_addskills():
	data={}
	s="select * from skills"
	data['skillview']=select(s)
	if "sub" in request.form:
		fn=request.form['skill']
		
		qu="insert into skills values(null,'%s')"%(fn)
		insert(qu)
		return redirect(url_for('admin.admin_addskills'))
	if "action" in request.args:
		action=request.args['action']
		sid=request.args['sid']
	else:
		action=None
	if action=='delete':
		de="delete from skills where skills_id='%s'"%(sid)
		delete(de)
		return redirect(url_for('admin.admin_addskills'))
	if action=='update':
		up="select * from skills where skills_id='%s'"%(sid)
		data['skillupdate']=select(up)
		if "sub1" in request.form:
			fn=request.form['skill']
			upd="update skills set skills='%s' where skills_id='%s'"%(fn,sid)
			update(upd)
			return redirect(url_for('admin.admin_addskills'))

	return render_template('admin_addskills.html',data=data)

@admin.route('/admin_managebadge',methods=['get','post'])
def admin_managebadge():
	data={}
	s="select * from badgeorchive"
	data['catview']=select(s)
	if "sub" in request.form:
		fn=request.form['category']
		
		qu="insert into badgeorchive values(null,'%s')"%(fn)
		insert(qu)
		return redirect(url_for('admin.admin_managebadge'))
	if "action" in request.args:
		action=request.args['action']
		cid=request.args['cid']
	else:
		action=None
	if action=='delete':
		de="delete from badgeorchive where badgeorchive_id='%s'"%(cid)
		delete(de)
		return redirect(url_for('admin.admin_managebadge'))
	if action=='update':
		up="select * from badgeorchive where badgeorchive_id='%s'"%(cid)
		data['catupdate']=select(up)
		if "sub1" in request.form:
			fn=request.form['category']
			upd="update badgeorchive set badgeorchive='%s' where badgeorchive_id='%s'"%(fn,cid)
			update(upd)
			return redirect(url_for('admin.admin_managebadge'))

	return render_template('admin_managebadge.html',data=data)
@admin.route('/admin_viewusers',methods=['get','post'])
def admin_viewusers():
	data={}
	s="select * from user"
	data['userview']=select(s)
	return render_template('admin_viewusers.html',data=data)
@admin.route('/adminuserskill',methods=['get','post'])
def adminuserskill():
	uid=request.args['uid']
	data={}
	s=" SELECT * FROM myskills INNER JOIN `user` USING(user_id) INNER JOIN `skills` USING(skills_id) where user_id='%s'"%(uid)
	data['myskillview']=select(s)
	return render_template('adminusersskill.html',data=data)

@admin.route('/viewcomplaint',methods=['get','post'])
def viewcomplaint():
	data={}
	q="select * from complaint inner join user using(user_id)"
	res=select(q)
	data['viewcomplaint']=res
	j=0
	for i in range(1,len(res)+1):
		if 'submit'+str(i) in request.form:
			reply=request.form['reply'+str(i)]
			q="update complaint set reply='%s' where complaint_id='%s'"%(reply,res[j]['complaint_id'])
			update(q)
			flash("message send")
			return redirect(url_for("admin.viewcomplaint"))
		j=j+1
	return render_template("adminview_complaints.html",data=data)





