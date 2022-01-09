import markdownToAnsi from 'markdown-to-ansi'
import fs from 'fs'
var filePath = ''
var outputFilePath = ''

process.argv.forEach(function (val, index, array) {
  if (index == 2) {
    filePath = val
  }
  if (index == 3) {
      outputFilePath = val
  }
});

const markdown = fs.readFileSync(filePath, 'utf8')
const transform = markdownToAnsi()
const result = transform(markdown)

if (outputFilePath != '') {
    fs.writeFile(outputFilePath, result, (err) => {
      if (err)
        console.log(err);
    })
} else {
    console.log(result)
}

