from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/gene')
def gene():
    return render_template('gene.html')

@app.route('/gene/<int:tid>')
def gene_topic(tid=None):
    if tid:
        return render_template('30760.html')
    else:
        return ""

app.debug = True
app.run(host='192.168.0.100')