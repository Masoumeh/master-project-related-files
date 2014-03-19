'MIN X: ' || min(
  for $x in //*:coordinates
  for $xy in tokenize($x, ' ')
  return number(tokenize($xy, ',')[1])
),
'MAX X: ' || 
max(
  for $x in //*:coordinates
  for $xy in tokenize($x, ' ')
  return number(tokenize($xy, ',')[1])
),
'MIN Y: ' || 
min(
  for $x in //*:coordinates
  for $xy in tokenize($x, ' ')
  return number(tokenize($xy, ',')[2])
),
'MAX Y: ' || 
max(
  for $x in //*:coordinates
  for $xy in tokenize($x, ' ')
  return number(tokenize($xy, ',')[2])
)
