const express = require('express');
const hbs = require('express-handlebars');
const bodyParser = require("body-parser");
const app = express();
app.use(express.static('public'));
app.engine('hbs', hbs.engine({
    layoutsDir: __dirname + '/views/layouts',
    defaultLayout: 'main',
    extname: '.hbs'
}));
app.set('view engine', 'hbs');
app.use(bodyParser.urlencoded({
    extended: true
}))
app.use(bodyParser.json())
app.get('/', (req, res) => {
    let query = "select ReadingKanji, ReadingKana, Pos, group_concat(English SEPARATOR ', ') as English2  from dictionary group by ReadingKanji, ReadingKana, Pos order by rand() limit 10";
    let items = []
    con.query(query, (err, result) => {
        if (err) throw err;
        items = result
        console.log(items)
        res.render('index', {
            items: items
        })
    })
});

app.post('/', (req, res) => {
    console.log(req.body)
    let query = "select * from mergedWithIndex where SeqID in (select SeqID from frenglishref natural join frenglish where English like ?) ORDER BY length(English2) ASC limit 1000";
    data = ['%'+req.body.task+'%']
    
    con.query(query, [data], (err, result) => {
        if (err) throw err;
        items = result
        console.log(items)
        res.render('index', {
            items: items
        })
    })
})
app.post('/kanji', (req, res) => {
    console.log(req.body)
    let query = "select ReadingKanji, ReadingKana, Pos, group_concat(English SEPARATOR ', ') as English2  from dictionary where ReadingKanji like ? group by ReadingKanji, ReadingKana, Pos  ORDER BY length(ReadingKanji) ASC limit 1000";
    data = ['%'+req.body.task+'%']
    
    con.query(query, [data], (err, result) => {
        if (err) throw err;
        items = result
        console.log(items)
        res.render('index', {
            items: items
        })
    })
})
app.post('/kana', (req, res) => {
    console.log(req.body)
    let query = "select ReadingKanji, ReadingKana, Pos, group_concat(English SEPARATOR ', ') as English2  from dictionary where ReadingKana like ? group by ReadingKanji, ReadingKana, Pos  ORDER BY length(ReadingKana) ASC limit 1000";
    data = ['%'+req.body.task+'%']
    
    con.query(query, [data], (err, result) => {
        if (err) throw err;
        items = result
        console.log(items)
        res.render('index', {
            items: items
        })
    })
})
// port where app is served
app.listen(3000, () => {
    console.log('The web server has started on port 3000');
});
const con = require('./models/taskModel')

