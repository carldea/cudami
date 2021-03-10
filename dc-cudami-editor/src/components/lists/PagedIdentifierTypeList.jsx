import React from 'react'
import {useTranslation} from 'react-i18next'
import {Button, Card, CardBody, Col, Row, Table} from 'reactstrap'

import {typeToEndpointMapping} from '../../api'
import usePagination from '../../hooks/usePagination'
import Pagination from '../Pagination'
import ActionButtons from './ActionButtons'

const PagedIdentifierTypeList = ({apiContextPath = '/', mockApi = false}) => {
  const type = 'identifierType'
  const {
    content: identifierTypes,
    numberOfPages,
    pageNumber,
    setPageNumber,
    totalElements,
  } = usePagination(apiContextPath, mockApi, type)
  const {t} = useTranslation()
  return (
    <>
      <Row>
        <Col>
          <h1>{t('identifierTypes')}</h1>
        </Col>
        <Col className="text-right">
          <Button href={`${apiContextPath}${typeToEndpointMapping[type]}/new`}>
            {t('new')}
          </Button>
        </Col>
      </Row>
      <hr />
      <Card className="border-top-0">
        <CardBody>
          <Pagination
            changePage={({selected}) => setPageNumber(selected)}
            numberOfPages={numberOfPages}
            pageNumber={pageNumber}
            totalElements={totalElements}
            type={type}
          />
          <Table bordered className="mb-0" hover responsive size="sm" striped>
            <thead>
              <tr>
                <th className="text-center">{t('label')}</th>
                <th className="text-center">{t('namespace')}</th>
                <th className="text-center">{t('pattern')}</th>
                <th className="text-center">{t('actions')}</th>
              </tr>
            </thead>
            <tbody>
              {identifierTypes.map(({label, namespace, pattern, uuid}) => (
                <tr key={uuid}>
                  <td>{label}</td>
                  <td>{namespace}</td>
                  <td>{pattern}</td>
                  <td className="text-center">
                    <ActionButtons
                      editUrl={`${apiContextPath}${typeToEndpointMapping[type]}/${uuid}/edit`}
                      showEdit
                      showView={false}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
          <Pagination
            changePage={({selected}) => setPageNumber(selected)}
            numberOfPages={numberOfPages}
            pageNumber={pageNumber}
            position="under"
            showTotalElements={false}
            totalElements={totalElements}
            type={type}
          />
        </CardBody>
      </Card>
    </>
  )
}

export default PagedIdentifierTypeList
