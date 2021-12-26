import markdownToAnsi from 'markdown-to-ansi'
import fs from 'fs'

const markdown = fs.readFileSync('README.md', 'utf8')
const transform = markdownToAnsi()
const result = transform(markdown)

console.log(result)