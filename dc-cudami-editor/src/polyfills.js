import 'babel-polyfill'
import fromEntries from 'object.fromentries'

if (!Object.fromEntries) {
  fromEntries.shim()
}
