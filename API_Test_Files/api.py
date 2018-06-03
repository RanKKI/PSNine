from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/gene')
def gene():
    return render_template('gene.html')

app.debug = True
app.run(host='192.168.0.100')