var mysql = require('mysql');
var con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "C20431112c"
});
con.connect(function(err) {
  if (err) throw err;
  console.log("Connected to the database!");
  let query ="use nihongo; ";
  con.query(query, (err, result)=>{
      if (err) throw err;
      console.log(result)
  })
});

module.exports = con;