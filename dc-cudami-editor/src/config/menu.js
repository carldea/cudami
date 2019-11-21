import {
  joinUp,
  lift,
  setBlockType,
  toggleMark,
  wrapIn,
} from 'prosemirror-commands'
import {redo, undo} from 'prosemirror-history'
import {wrapInList} from 'prosemirror-schema-list'
// import { addColumnAfter, addColumnBefore } from 'prosemirror-tables'
import {publish, subscribe, unsubscribe} from 'pubsub-js'

import schema from './schema'
import icons from './icons'

const blockActive = (type, attrs = {}) => state => {
  const {$from, to, node} = state.selection

  if (node) {
    return node.hasMarkup(type, attrs)
  }

  return to <= $from.end() && $from.parent.hasMarkup(type, attrs)
}

const canInsert = type => state => {
  const {$from} = state.selection

  for (let d = $from.depth; d >= 0; d--) {
    const index = $from.index(d)

    if ($from.node(d).canReplaceWith(index, index, type)) {
      return true
    }
  }

  return false
}

const headingActive = () => state => {
  let active = false
  const levels = [1, 2, 3, 4, 5, 6]
  for (let level of levels) {
    if (blockActive(schema.nodes.heading, {level})(state)) {
      active = true
      break
    }
  }
  return active
}

const markActive = type => state => {
  const {from, $from, to, empty} = state.selection

  return empty
    ? type.isInSet(state.storedMarks || $from.marks())
    : state.doc.rangeHasMark(from, to, type)
}

const promptForURL = () => {
  let url = window && window.prompt('Enter the URL', 'https://')

  if (url && !/^https?:\/\//i.test(url)) {
    url = 'http://' + url
  }

  return url
}

export default function(t) {
  return {
    marks: {
      strong: {
        title: t('marks.strong'),
        content: icons.strong,
        active: markActive(schema.marks.strong),
        run: toggleMark(schema.marks.strong),
      },
      em: {
        title: t('marks.em'),
        content: icons.em,
        active: markActive(schema.marks.em),
        run: toggleMark(schema.marks.em),
      },
      underline: {
        title: t('marks.underline'),
        content: icons.underline,
        active: markActive(schema.marks.underline),
        run: toggleMark(schema.marks.underline),
      },
      strikethrough: {
        title: t('marks.strikethrough'),
        content: icons.strikethrough,
        active: markActive(schema.marks.strikethrough),
        run: toggleMark(schema.marks.strikethrough),
      },
      superscript: {
        title: t('marks.superscript'),
        content: icons.superscript,
        active: markActive(schema.marks.superscript),
        run: toggleMark(schema.marks.superscript),
      },
      subscript: {
        title: t('marks.subscript'),
        content: icons.subscript,
        active: markActive(schema.marks.subscript),
        run: toggleMark(schema.marks.subscript),
      },
      code: {
        title: t('marks.code'),
        content: icons.code,
        active: markActive(schema.marks.code),
        run: toggleMark(schema.marks.code),
      },
      link: {
        title: t('marks.link'),
        content: icons.link,
        active: markActive(schema.marks.link),
        enable: state => !state.selection.empty,
        run(state, dispatch) {
          if (markActive(schema.marks.link)(state)) {
            toggleMark(schema.marks.link)(state, dispatch)
            return true
          }

          const token = subscribe('editor.add-link', (_msg, data) => {
            if (!data.href) {
              return false
            }
            toggleMark(schema.marks.link, data)(state, dispatch)
            unsubscribe(token)
          })
          publish('editor.show-link-modal')
        },
      },
    },
    blocks: {
      plain: {
        title: t('blocks.paragraph'),
        content: icons.paragraph,
        active: blockActive(schema.nodes.paragraph),
        enable: setBlockType(schema.nodes.paragraph),
        run: setBlockType(schema.nodes.paragraph),
      },
      code_block: {
        title: t('blocks.codeBlock'),
        content: icons.code_block,
        active: blockActive(schema.nodes.code_block),
        enable: setBlockType(schema.nodes.code_block),
        run: setBlockType(schema.nodes.code_block),
      },
      heading: {
        title: t('blocks.heading'),
        content: icons.heading,
        active: headingActive(),
        enable: () => true,
        children: [
          {
            active: blockActive(schema.nodes.heading, {level: 1}),
            content: t('blocks.headingLevel', {level: 1}),
            enable: setBlockType(schema.nodes.heading, {level: 1}),
            run: setBlockType(schema.nodes.heading, {level: 1}),
          },
          {
            active: blockActive(schema.nodes.heading, {level: 2}),
            content: t('blocks.headingLevel', {level: 2}),
            enable: setBlockType(schema.nodes.heading, {level: 2}),
            run: setBlockType(schema.nodes.heading, {level: 2}),
          },
          {
            active: blockActive(schema.nodes.heading, {level: 3}),
            content: t('blocks.headingLevel', {level: 3}),
            enable: setBlockType(schema.nodes.heading, {level: 3}),
            run: setBlockType(schema.nodes.heading, {level: 3}),
          },
          {
            active: blockActive(schema.nodes.heading, {level: 4}),
            content: t('blocks.headingLevel', {level: 4}),
            enable: setBlockType(schema.nodes.heading, {level: 4}),
            run: setBlockType(schema.nodes.heading, {level: 4}),
          },
          {
            active: blockActive(schema.nodes.heading, {level: 5}),
            content: t('blocks.headingLevel', {level: 5}),
            enable: setBlockType(schema.nodes.heading, {level: 5}),
            run: setBlockType(schema.nodes.heading, {level: 5}),
          },
          {
            active: blockActive(schema.nodes.heading, {level: 6}),
            content: t('blocks.headingLevel', {level: 6}),
            enable: setBlockType(schema.nodes.heading, {level: 6}),
            run: setBlockType(schema.nodes.heading, {level: 6}),
          },
        ],
      },
      blockquote: {
        title: t('blocks.blockquote'),
        content: icons.blockquote,
        active: blockActive(schema.nodes.blockquote),
        enable: wrapIn(schema.nodes.blockquote),
        run: wrapIn(schema.nodes.blockquote),
      },
      bullet_list: {
        title: t('blocks.bulletList'),
        content: icons.bullet_list,
        active: blockActive(schema.nodes.bullet_list),
        enable: wrapInList(schema.nodes.bullet_list),
        run: wrapInList(schema.nodes.bullet_list),
      },
      ordered_list: {
        title: t('blocks.orderedList'),
        content: icons.ordered_list,
        active: blockActive(schema.nodes.ordered_list),
        enable: wrapInList(schema.nodes.ordered_list),
        run: wrapInList(schema.nodes.ordered_list),
      },
      lift: {
        title: t('blocks.lift'),
        content: icons.lift,
        enable: lift,
        run: lift,
      },
      join_up: {
        title: t('blocks.joinUp'),
        content: icons.join_up,
        enable: joinUp,
        run: joinUp,
      },
    },
    insert: {
      /*image: {
        title: t('insert.image'),
        content: icons.image,
        enable: canInsert(schema.nodes.image),
        run: (state, dispatch) => {
          const src = promptForURL()
          if (!src) return false

          const img = schema.nodes.image.createAndFill({src})
          dispatch(state.tr.replaceSelectionWith(img))
        },
      },
      footnote: {
        title: t('insert.footnote'),
        content: icons.footnote,
        enable: canInsert(schema.nodes.footnote),
        run: (state, dispatch) => {
          const footnote = schema.nodes.footnote.create()
          dispatch(state.tr.replaceSelectionWith(footnote))
        },
      },*/
      hr: {
        title: t('insert.hr'),
        content: icons.hr,
        enable: canInsert(schema.nodes.horizontal_rule),
        run: (state, dispatch) => {
          const hr = schema.nodes.horizontal_rule.create()
          dispatch(state.tr.replaceSelectionWith(hr))
        },
      },
      iframe: {
        title: t('insert.iframe'),
        content: icons.iframe,
        enable: canInsert(schema.nodes.iframe),
        run: (state, dispatch) => {
          const token = subscribe('editor.add-iframe', (_msg, data) => {
            const iframe = schema.nodes.iframe.createAndFill(data)
            dispatch(state.tr.replaceSelectionWith(iframe))
            unsubscribe(token)
          })
          publish('editor.show-iframe-modal')
        },
      },
      table: {
        title: t('insert.table'),
        content: icons.table,
        enable: canInsert(schema.nodes.table),
        run: (state, dispatch) => {
          const token = subscribe('editor.add-table', (_msg, data) => {
            let columnCount = data.columns
            const cells = []
            while (columnCount--) {
              cells.push(schema.nodes.table_cell.createAndFill())
            }

            let rowCount = data.rows
            const rows = []
            while (rowCount--) {
              rows.push(schema.nodes.table_row.createAndFill(null, cells))
            }

            const table = schema.nodes.table.createAndFill(null, rows)
            dispatch(state.tr.replaceSelectionWith(table))
            unsubscribe(token)
          })
          publish('editor.show-table-modal')
        },
      },
    },
    history: {
      undo: {
        title: t('history.undo'),
        content: icons.undo,
        enable: undo,
        run: undo,
      },
      redo: {
        title: t('history.redo'),
        content: icons.redo,
        enable: redo,
        run: redo,
      },
    },
    // table: {
    // addColumnBefore: {
    //   title: 'Insert column before',
    //   content: icons.after,
    //   active: addColumnBefore, // TOOD: active -> select
    //   run: addColumnBefore
    // },
    // addColumnAfter: {
    //   title: 'Insert column before',
    //   content: icons.before,
    //   active: addColumnAfter, // TOOD: active -> select
    //   run: addColumnAfter
    // }
    // }
  }
}
