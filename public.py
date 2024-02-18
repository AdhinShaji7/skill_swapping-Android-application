from flask import *
from database import *

public=Blueprint('public',__name__)
@public.route('/')
def home():
	return render_template('home.html')
@public.route('/login',methods=['post','get'])
def login():
	if "sub" in request.form:
		u=request.form['uname']
		p=request.form['pwd']
		s="select * from login where username='%s' and password='%s'" %(u,p)
		res=select(s)
		if res:
			session['login_id']=res[0]['login_id']
			print('my login',session['login_id'])

			if res[0]['usertype']=='admin':
				return redirect(url_for('admin.adminhome'))

		else:
			flash("username or password is incorrect")

	return render_template('login.html')